package deusto.safebox.server.net;

import deusto.safebox.common.net.packet.DisconnectPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.RequestLoginPacket;
import deusto.safebox.common.net.packet.SaveDataPacket;
import deusto.safebox.common.net.packet.TestPacket;
import deusto.safebox.server.dao.DaoManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    PacketHandler(Server server, DaoManager daoManager) {
        this.server = server;
        this.daoManager = daoManager;

        put(TestPacket.class, this::onTest);
        put(DisconnectPacket.class, this::onDisconnect);
        put(RequestLoginPacket.class, this::onRequestLogin);
        put(SaveDataPacket.class, this::onSaveData);
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
        // TODO
    }

    private void onSaveData(ClientHandler client, SaveDataPacket packet) {
        // TODO
    }
}
