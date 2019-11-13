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

/** Received packet handler. */
class PacketHandler {

    private final Server server;
    private final DaoManager daoManager;

    // Both wildcards must be of the same type.
    private final Map<Class<? extends Packet>, BiConsumer<ClientHandler, ? extends Packet>> map = new HashMap<>();

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
        map.put(Objects.requireNonNull(packetClass), consumer);
    }

    /**
     * Returns the operation associated to a packet type.
     *
     * @param object a packet of the same class as the desired operation.
     * @param <T> the type of the packet class.
     * @return the operation associated to the packet type,
     *          or an empty {@link Optional} if there is not associated operation.
     */
    <T extends Packet> Optional<BiConsumer<ClientHandler, T>> of(T object) {
        // Type safe because there is a type relationship between keys and values.
        @SuppressWarnings("unchecked")
        BiConsumer<ClientHandler, T> consumer = (BiConsumer<ClientHandler, T>) map.get(object.getClass());
        return Optional.ofNullable(consumer);
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
