package deusto.safebox.server.net;

import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.EMAIL_ALREADY_IN_USE;
import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.INVALID_LOGIN;
import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.REGISTER_ERROR;
import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType.UNKNOWN_ERROR;

import deusto.safebox.common.net.packet.DisconnectPacket;
import deusto.safebox.common.net.packet.LogOutPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.ReceiveDataPacket;
import deusto.safebox.common.net.packet.RequestLoginPacket;
import deusto.safebox.common.net.packet.RequestRegisterPacket;
import deusto.safebox.common.net.packet.SaveDataPacket;
import deusto.safebox.common.net.packet.SuccessfulRegisterPacket;
import deusto.safebox.common.net.packet.TestPacket;
import deusto.safebox.server.ItemCollection;
import deusto.safebox.server.User;
import deusto.safebox.server.dao.DaoException;
import deusto.safebox.server.dao.DaoManager;
import deusto.safebox.server.security.Argon2Hashing;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
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

    private static final int ARGON2_SERVER_ITERATIONS = 4;

    private final Server server;
    private final DaoManager daoManager;
    // Both wildcards must be of the same type.
    private final Map<Class<? extends Packet>, BiConsumer<ClientHandler, ? extends Packet>> packetMap = new HashMap<>();
    private final Map<ClientHandler, UUID> authenticatedUsers = Collections.synchronizedMap(new HashMap<>());

    PacketHandler(Server server, DaoManager daoManager) {
        this.server = server;
        this.daoManager = daoManager;

        put(TestPacket.class, this::onTest);
        put(DisconnectPacket.class, this::onDisconnect);
        put(RequestLoginPacket.class, this::onRequestLogin);
        put(RequestRegisterPacket.class, this::onRequestRegister);
        put(SaveDataPacket.class, this::onSaveData);
        put(LogOutPacket.class, this::onLogOut);
    }

    /**
     * Adds the operation for a packet type.
     *
     * @param packetClass the packet class type.
     * @param consumer the operation for the packet.
     * @param <T> the type of the packet class.
     */
    private <T extends Packet> void put(Class<T> packetClass, BiConsumer<ClientHandler, T> consumer) {
        packetMap.put(Objects.requireNonNull(packetClass), consumer);
    }

    /** Calls the action for the specified client and packet. */
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

    private void onTest(ClientHandler client, TestPacket packet) {
        System.out.println("Test packet action.");
    }

    private void onDisconnect(ClientHandler client, DisconnectPacket packet) {
        server.removeClient(client);
    }

    private void onRequestLogin(ClientHandler client, RequestLoginPacket packet) {
        Optional<User> optionalUser;
        try {
            optionalUser = daoManager.getUserDao().getByEmail(packet.getEmail());
        } catch (DaoException e) {
            logger.error("Error getting user by email", e);
            return;
        }
        optionalUser.ifPresentOrElse(user -> {
            if (!Argon2Hashing.verify(user.getPassword(), packet.getPassword())) {
                client.sendPacket(INVALID_LOGIN.get());
                return;
            }
            try {
                daoManager.getItemCollectionDao().get(user.getId()).ifPresentOrElse(
                        itemCollection -> {
                            client.sendPacket(new ReceiveDataPacket(itemCollection.getItems()));
                            authenticatedUsers.put(client, user.getId());
                        },
                        () -> client.sendPacket(UNKNOWN_ERROR.get()));
            } catch (DaoException e) {
                logger.error("Error getting item collection from " + user.getId(), e);
            }
        }, () -> client.sendPacket(INVALID_LOGIN.get()));
    }

    private void onRequestRegister(ClientHandler client, RequestRegisterPacket packet) {
        try {
            if (daoManager.getUserDao().getByEmail(packet.getEmail()).isPresent()) {
                client.sendPacket(EMAIL_ALREADY_IN_USE.get());
                return;
            }
        } catch (DaoException e) {
            logger.error("Error getting user by email", e);
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

    private void onSaveData(ClientHandler client, SaveDataPacket packet) {
        if (!authenticatedUsers.containsKey(client)) {
            client.sendPacket(UNKNOWN_ERROR.get());
            return;
        }
        UUID userId = authenticatedUsers.get(client);
        try {
            if (!daoManager.getItemCollectionDao().insert(new ItemCollection(userId, packet.getItems()))) {
                client.sendPacket(UNKNOWN_ERROR.get());
            }
        } catch (DaoException e) {
            logger.error("Error inserting items to " + userId, e);
            client.sendPacket(UNKNOWN_ERROR.get());
        }
    }

    private void onLogOut(ClientHandler client, LogOutPacket packet) {
        authenticatedUsers.remove(client);
    }
}
