package deusto.safebox.server.net;

import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.EMAIL_ALREADY_IN_USE;
import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.INVALID_LOGIN;
import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.REGISTER_ERROR;
import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.SAVE_DATA_ERROR;
import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.UNKNOWN_ERROR;

import deusto.safebox.common.net.packet.DisconnectPacket;
import deusto.safebox.common.net.packet.LogOutPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.RequestLoginPacket;
import deusto.safebox.common.net.packet.RequestRegisterPacket;
import deusto.safebox.common.net.packet.RetrieveDataPacket;
import deusto.safebox.common.net.packet.SaveDataPacket;
import deusto.safebox.common.net.packet.SuccessfulRegisterPacket;
import deusto.safebox.common.net.packet.SuccessfulSaveDataPacket;
import deusto.safebox.server.ItemCollection;
import deusto.safebox.server.User;
import deusto.safebox.server.dao.DaoManager;
import deusto.safebox.server.security.Argon2Hashing;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Received packet handler. */
class PacketHandler {

    private static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);

    /** Iteration count used to hash the passwords. */
    private static final int ARGON2_SERVER_ITERATIONS = 4;

    private final Server server;
    private final DaoManager daoManager;

    // Both wildcards must be of the same type.
    private final Map<Class<?>, BiConsumer<ClientHandler, ?>> packetMap = new HashMap<>();

    /** Map containing the clients that are currently logged in. */
    private final Map<ClientHandler, UUID> authenticatedUsers = Collections.synchronizedMap(new HashMap<>());

    PacketHandler(Server server, DaoManager daoManager) {
        this.server = server;
        this.daoManager = daoManager;
        registerListeners();
    }

    int getAuthenticatedClientCount() {
        return authenticatedUsers.size();
    }

    /** Runs the operation for the specified client and packet. */
    <T extends Packet> void fire(ClientHandler client, T packet) {
        // Type safe because there is a type relationship between keys and values.
        @SuppressWarnings("unchecked")
        BiConsumer<ClientHandler, T> consumer = (BiConsumer<ClientHandler, T>) packetMap.get(packet.getClass());
        Optional.ofNullable(consumer)
                .ifPresentOrElse(
                        c -> c.accept(client, packet),
                        () -> logger.error(
                                "There is no action defined for the received packet ({}).",
                                packet.getClass().getName())
                );
    }

    @EventListener
    private void onDisconnect(ClientHandler client, DisconnectPacket packet) {
        authenticatedUsers.remove(client);
        server.removeClient(client);
    }

    @EventListener
    private void onRequestLogin(ClientHandler client, RequestLoginPacket packet) {
        daoManager.getUserDao().getByEmail(packet.getEmail())
            .thenAccept(optionalUser -> optionalUser.ifPresentOrElse(
                user -> Argon2Hashing.verify(user.getPassword(), packet.getPassword())
                        .thenAccept(result -> {
                            if (result) {
                                sendDataToUser(client, user);
                            } else {
                                client.sendPacket(INVALID_LOGIN.get());
                                logger.debug("Invalid login for " + user.getEmail());
                            }
                        }),
                () -> { // no user found with that email address
                    logger.debug("Invalid login by {}.", packet.getEmail());
                    client.sendPacket(INVALID_LOGIN.get());
                }
            ))
            .exceptionally(e -> {
                logger.error("Error getting a user by their email.", e);
                client.sendPacket(UNKNOWN_ERROR.get());
                return null;
            });
    }

    private void sendDataToUser(ClientHandler client, User user) {
        daoManager.getItemCollectionDao().get(user.getId())
            .thenAccept(optional -> optional.ifPresentOrElse(
                itemCollection -> {
                    authenticatedUsers.put(client, user.getId());
                    client.sendPacket(new RetrieveDataPacket(itemCollection.getItems()));
                },
                () -> {
                    logger.error("No item collection found for {}.", user.getId());
                    client.sendPacket(UNKNOWN_ERROR.get());
                }
            ))
            .exceptionally(e -> {
                logger.error("Error getting the item collection for {}.", user.getId());
                client.sendPacket(UNKNOWN_ERROR.get());
                return null;
            });
    }

    @EventListener
    private void onRequestRegister(ClientHandler client, RequestRegisterPacket packet) {
        daoManager.getUserDao().getByEmail(packet.getEmail())
            .thenAccept(optional -> optional.ifPresentOrElse(
                ignored -> {
                    client.sendPacket(EMAIL_ALREADY_IN_USE.get());
                }, () -> {
                    performRegister(client, packet);
                })
            )
            .exceptionally(e -> {
                logger.error("Error getting user by email", e);
                client.sendPacket(UNKNOWN_ERROR.get());
                return null;
            });
    }

    private void performRegister(ClientHandler client, RequestRegisterPacket packet) {
        Argon2Hashing.hash(packet.getPassword(), ARGON2_SERVER_ITERATIONS)
                .thenApply(hash ->
                        new User(UUID.randomUUID(), packet.getName(), packet.getEmail(), hash, LocalDate.now()))
                .thenCompose(user -> daoManager.getUserDao().insert(user))
                .thenAccept(result -> {
                    if (result) {
                        client.sendPacket(new SuccessfulRegisterPacket());
                    } else {
                        logger.error("Error registering a user.");
                        client.sendPacket(REGISTER_ERROR.get());
                    }
                });
    }

    @EventListener
    private void onSaveData(ClientHandler client, SaveDataPacket packet) {
        if (!authenticatedUsers.containsKey(client)) {
            client.sendPacket(UNKNOWN_ERROR.get());
            return;
        }
        UUID userId = authenticatedUsers.get(client);

        daoManager.getItemCollectionDao().insert(new ItemCollection(userId, packet.getItems()))
                .thenAccept(result -> {
                    if (result) {
                        client.sendPacket(new SuccessfulSaveDataPacket());
                    } else {
                        client.sendPacket(SAVE_DATA_ERROR.get());
                        logger.error("Unknown error inserting items from user " + userId);
                    }
                });
    }

    @EventListener
    private void onLogOut(ClientHandler client, LogOutPacket packet) {
        authenticatedUsers.remove(client);
    }

    private void registerListeners() {
        Method[] publicMethods = getClass().getMethods();
        Method[] privateMethods = getClass().getDeclaredMethods();
        Collection<Method> methods = new HashSet<>();
        methods.addAll(Arrays.asList(publicMethods));
        methods.addAll(Arrays.asList(privateMethods));

        for (Method method : methods) {
            if (!method.isAnnotationPresent(EventListener.class)) {
                continue;
            }

            Class<?> checkClass;
            if (method.getParameterTypes().length != 2
                    || !Packet.class.isAssignableFrom(checkClass = method.getParameterTypes()[1])
                    || !ClientHandler.class.isAssignableFrom(method.getParameterTypes()[0])) {
                logger.error("Attempted to register an invalid EventListener method signature.");
                continue;
            }
            method.setAccessible(true);

            BiConsumer<ClientHandler, ? extends Packet> consumer = (clientHandler, packet) -> {
                try {
                    method.invoke(this, clientHandler, packet);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            };

            packetMap.put(Objects.requireNonNull(checkClass), consumer);
        }
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface EventListener {}
}
