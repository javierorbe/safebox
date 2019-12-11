package deusto.safebox.common.net;

import deusto.safebox.common.net.packet.Packet;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;
import javax.net.ssl.SSLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles a socket that communicates through {@link Packet} objects.
 */
public abstract class SocketHandler extends Thread implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    private volatile boolean running = false;
    private ObjectOutputStream out;

    private Runnable connectionEstablished;
    private Consumer<Packet> packetReceived;

    /** Creates a socket handler with the default behavior. */
    protected SocketHandler() {
        this.connectionEstablished = () -> LOGGER.info("Socket connection established.");
        this.packetReceived = packet -> LOGGER.info("Packet received ({}).", packet);
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * Set the connection established callback.
     *
     * @param connectionEstablished the callback.
     */
    public void setConnectionEstablished(Runnable connectionEstablished) {
        this.connectionEstablished = connectionEstablished;
    }

    /**
     * Set the packet received callback.
     *
     * @param packetReceived the callback.
     */
    public void setPacketReceived(Consumer<Packet> packetReceived) {
        this.packetReceived = packetReceived;
    }

    /** Starts listening to incoming data from the socket. */
    protected void listen() {
        running = true;

        try {
            out = new ObjectOutputStream(getSocket().getOutputStream());
        } catch (IOException e) {
            LOGGER.error("Could not initialize the output stream.", e);
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(getSocket().getInputStream())) {
            connectionEstablished.run();

            while (getSocket().isConnected()) {
                try {
                    Object data = in.readObject();
                    if (data instanceof Packet) {
                        packetReceived.accept((Packet) data);
                    } else {
                        LOGGER.warn("Received an invalid object type ({})", data.getClass().getName());
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.error("Could not deserialize the received object.", e);
                }
            }
        } catch (EOFException | SSLException e) { // This exception is thrown when the input stream is closed.
            if (running) {
                close();
            }
        } catch (IOException e) {
            LOGGER.error("Error while listening to the socket.", e);
        }
    }

    /** Closes the socket. */
    @Override
    public synchronized void close() {
        running = false;
        try {
            getSocket().close();
            LOGGER.debug("Socket closed.");
        } catch (IOException e) {
            LOGGER.error("Error closing the socket.", e);
        }
    }

    /**
     * Supplies the socket that is handled by this class.
     *
     * @return the socket handled by this class.
     */
    protected abstract Socket getSocket();

    /**
     * Send a packet to the other endpoint of the socket connection.
     * This method shouldn't be used without receiving the {@link #connectionEstablished} callback.
     *
     * @param packet the packet to send.
     */
    public synchronized void sendPacket(Packet packet) {
        try {
            out.writeObject(packet);
            LOGGER.trace("Packet sent: {}", packet);
        } catch (IOException e) {
            LOGGER.error("Error sending packet.", e);
        }
    }
}
