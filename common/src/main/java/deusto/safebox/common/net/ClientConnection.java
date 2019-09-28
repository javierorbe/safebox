package deusto.safebox.common.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Represents an end side of the socket connection.
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
            e.printStackTrace();
        } finally {
            if (getSocket() != null && !getSocket().isClosed()) {
                try {
                    getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
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
            logger.error("Error while closing ClientConnection", e);
        }
    }

    protected abstract Socket getSocket();

    public void sendPacket(byte[] packet) throws IOException {
        getSocket().getOutputStream().write(packet);
        getSocket().getOutputStream().flush();
    }

    /** Called when the connection is ready. */
    protected abstract void connectionEstablished();

    protected abstract void receivePacket(byte[] packet);
}
