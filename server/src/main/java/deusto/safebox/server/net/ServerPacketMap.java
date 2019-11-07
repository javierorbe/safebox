package deusto.safebox.server.net;

import deusto.safebox.common.net.packet.DisconnectPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.TestPacket;
import deusto.safebox.server.dao.DaoManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ServerPacketMap {

    private static final Logger logger = LoggerFactory.getLogger(ServerPacketMap.class);

    private final Server server;
    private final DaoManager daoManager;

    // Both wildcards must be of the same type.
    private final Map<Class<? extends Packet>, BiConsumer<ClientHandler, ? extends Packet>> map = new HashMap<>();

    ServerPacketMap(Server server, DaoManager daoManager) {
        this.server = server;
        this.daoManager = daoManager;

        put(TestPacket.class, this::onTest);
        put(DisconnectPacket.class, this::onDisconnect);
    }

    private <T extends Packet> void put(Class<T> packetClass, BiConsumer<ClientHandler, T> consumer) {
        map.put(Objects.requireNonNull(packetClass), consumer);
    }

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
}
