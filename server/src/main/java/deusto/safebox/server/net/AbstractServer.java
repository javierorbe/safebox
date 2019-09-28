package deusto.safebox.server.net;

import deusto.safebox.common.net.ClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractServer extends Thread implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    private Set<ClientConnection> clients = new HashSet<>();
    private boolean running = false;

    void listen() {
        running = true;

        try {
            while (running) {
                Socket socket = getServerSocket().accept();
                ClientConnection client = getClientHandler(socket);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            logger.error("Error while server socket was listening.", e);
        } finally {
            if (!getServerSocket().isClosed()) {
                try {
                    getServerSocket().close();
                } catch (IOException e) {
                    logger.error("Error closing server socket.", e);
                }
            }
        }
    }

    @Override
    public void close() {
        running = false;
        clients.forEach(ClientConnection::close);
        try {
            getServerSocket().close();
            logger.debug("Server socket closed.");
        } catch (IOException e) {
            logger.error("Error while closing server socket.", e);
        }
    }

    protected abstract ServerSocket getServerSocket();

    protected abstract ClientConnection getClientHandler(Socket socket);
}
