package deusto.safebox.common.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an endpoint of the socket connection.
 */
public abstract class ClientConnection extends Thread implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    private static final int PACKET_BUFFER_SIZE = 8192;

    protected void listen() {
        try (InputStream in = new DataInputStream(getSocket().getInputStream())) {
            connectionEstablished();

            int size;
            byte[] buffer = new byte[PACKET_BUFFER_SIZE];
            while ((size = in.read(buffer)) > 0) {
                receivePacket(Arrays.copyOf(buffer, size));
            }
        } catch (IOException e) {
            logger.error("Error listening to the socket.", e);
        } finally {
            if (getSocket() != null && !getSocket().isClosed()) {
                try {
                    getSocket().close();
                } catch (IOException e) {
                    logger.error("Error closing client socket.", e);
                }
            }
        }
    }

    @Override
    public void close() {
        try {
            getSocket().close();
            logger.debug("Client connection closed.");
        } catch (IOException e) {
            logger.error("Error closing client connection.", e);
        }
    }

    protected abstract Socket getSocket();

    public void sendPacket(byte[] packet) throws IOException {
        getSocket().getOutputStream().write(packet);
        // TODO: is a flush necessary?
        getSocket().getOutputStream().flush();
    }

    /** Called when the connection is ready to send and receive data. */
    protected abstract void connectionEstablished();

    protected abstract void receivePacket(byte[] packet);
}
