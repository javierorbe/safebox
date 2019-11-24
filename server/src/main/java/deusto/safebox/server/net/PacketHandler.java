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
import deusto.safebox.common.net.packet.TestPacket;
import deusto.safebox.server.ItemCollection;
import deusto.safebox.server.User;
import deusto.safebox.server.dao.DaoException;
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
    private void onTest(ClientHandler client, TestPacket packet) {
        System.out.println("Test packet action.");
    }

    @EventListener
    private void onDisconnect(ClientHandler client, DisconnectPacket packet) {
        authenticatedUsers.remove(client);
        server.removeClient(client);
    }

    @EventListener
    private void onRequestLogin(ClientHandler client, RequestLoginPacket packet) {
        Optional<User> optionalUser;
        try {
            optionalUser = daoManager.getUserDao().getByEmail(packet.getEmail());
        } catch (DaoException e) {
            logger.error("Error getting user by email", e);
            return;
        }
        optionalUser.ifPresentOrElse(user -> {
            boolean auth = Argon2Hashing.verify(user.getPassword(), packet.getPassword());
            if (!auth) {
                client.sendPacket(INVALID_LOGIN.get());
                logger.debug("Invalid login for " + user.getEmail());
                return;
            }
            try {
                daoManager.getItemCollectionDao().get(user.getId()).ifPresentOrElse(
                        itemCollection -> {
                            client.sendPacket(new RetrieveDataPacket(itemCollection.getItems()));
                            authenticatedUsers.put(client, user.getId());
                        },
                        () -> client.sendPacket(UNKNOWN_ERROR.get()));
            } catch (DaoException e) {
                logger.error("Error getting item collection from " + user.getId(), e);
            }
        }, () -> client.sendPacket(INVALID_LOGIN.get()));
    }

    @EventListener
    private void onRequestRegister(ClientHandler client, RequestRegisterPacket packet) {
        try {
            if (daoManager.getUserDao().getByEmail(packet.getEmail()).isPresent()) {
                client.sendPacket(EMAIL_ALREADY_IN_USE.get());
                return;
            }
        } catch (DaoException e) {
            logger.error("Error getting user by email", e);
            client.sendPacket(UNKNOWN_ERROR.get());
            return;
        }

        String passHashed = Argon2Hashing.hash(packet.getPassword(), ARGON2_SERVER_ITERATIONS);
        User user = new User(UUID.randomUUID(), packet.getName(), packet.getEmail(), passHashed, LocalDate.now());

        try {
            if (daoManager.getUserDao().insert(user)) {
                client.sendPacket(new SuccessfulRegisterPacket());
            } else {
                client.sendPacket(REGISTER_ERROR.get());
            }
        } catch (DaoException e) {
            logger.error("Error inserting user", e);
            client.sendPacket(REGISTER_ERROR.get());
        }
    }

    @EventListener
    private void onSaveData(ClientHandler client, SaveDataPacket packet) {
        if (!authenticatedUsers.containsKey(client)) {
            client.sendPacket(UNKNOWN_ERROR.get());
            return;
        }
        UUID userId = authenticatedUsers.get(client);
        try {
            boolean insertResult = daoManager.getItemCollectionDao()
                    .insert(new ItemCollection(userId, packet.getItems()));
            if (insertResult) {
                client.sendPacket(new SuccessfulSaveDataPacket());
            } else {
                client.sendPacket(SAVE_DATA_ERROR.get());
                logger.error("Unknown error inserting items from user " + userId);
            }
        } catch (DaoException e) {
            logger.error("Error inserting items from user " + userId, e);
            client.sendPacket(SAVE_DATA_ERROR.get());
        }
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
            EventListener eh = method.getAnnotation(EventListener.class);
            if (eh == null) {
                continue;
            }

            Class<?> checkClass;
            if (method.getParameterTypes().length != 2
                    || !Packet.class.isAssignableFrom(checkClass = method.getParameterTypes()[1])
                    || !ClientHandler.class.isAssignableFrom(method.getParameterTypes()[0])) {
                logger.error("Attempted to register an invalid EventHandler method signature.");
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
