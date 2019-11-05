package deusto.safebox.server.net;

import deusto.safebox.common.net.SocketHandler;
import deusto.safebox.common.net.packet.DisconnectPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.TestPacket;
import deusto.safebox.common.util.BoundClassConsumerMap;
import deusto.safebox.common.util.IBoundClassConsumerMap;
import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClientHandler extends SocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final IBoundClassConsumerMap<Packet> packetAction = new BoundClassConsumerMap<>();
    private final SSLSocket socket;

    ClientHandler(SSLSocket socket) {
        this.socket = socket;

        packetAction.put(DisconnectPacket.class, packet -> disconnect());
        packetAction.put(TestPacket.class, packet -> System.out.println("Test packet action."));
    }

    @Override
    public void run() {
        try {
            socket.startHandshake();
        } catch (IOException e) {
            logger.error("Could not complete handshake.", e);
            return;
        }
        listen();
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    protected void connectionEstablished() {
        logger.trace("New client connection established.");
    }

    @Override
    protected void receivePacket(Packet packet) {
        logger.trace("Received a packet: {}", packet);
        packetAction.of(packet).ifPresentOrElse(
            action -> action.accept(packet),
            () -> logger.error("There is no action defined for the received packet ({})", packet)
        );
    }

    /** Callback for when the client is going to disconnect. */
    protected abstract void disconnect();
}
