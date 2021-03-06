package deusto.safebox.server.net;

import deusto.safebox.common.net.SocketHandler;
import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Handles the connection of a client. */
class ClientHandler extends SocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final SSLSocket socket;

    ClientHandler(SSLSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            socket.startHandshake();
        } catch (IOException e) {
            LOGGER.error("Could not complete handshake.", e);
            return;
        }
        listen();
    }

    @Override
    protected SSLSocket getSocket() {
        return socket;
    }
}
