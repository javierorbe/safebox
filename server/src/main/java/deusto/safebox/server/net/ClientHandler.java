package deusto.safebox.server.net;

import deusto.safebox.common.net.ClientConnection;
import deusto.safebox.common.util.JsonData;
import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends ClientConnection {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private SSLSocket socket;

    ClientHandler(SSLSocket socket) {
        this.socket = socket;
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
    protected void receivePacket(JsonData packet) {
        logger.trace("Received a packet: {}", packet);
    }
}
