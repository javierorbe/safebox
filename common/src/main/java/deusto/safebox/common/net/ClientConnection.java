package deusto.safebox.common.net;

import deusto.safebox.common.net.packet.Packet;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Represents an endpoint of the socket connection. */
public abstract class ClientConnection extends Thread implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    private boolean running = false;
    private ObjectOutputStream out;

    /** Starts listening to incoming data from the socket. */
    protected void listen() {
        running = true;

        try {
            out = new ObjectOutputStream(getSocket().getOutputStream());
        } catch (IOException e) {
            logger.error("Could not initialize the output stream.", e);
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(getSocket().getInputStream())) {
            connectionEstablished();

            while (getSocket().isConnected()) {
                try {
                    Object data = in.readObject();
                    if (data instanceof Packet) {
                        receivePacket((Packet) data);
                    } else {
                        logger.warn("Received an invalid object type ({})", data.getClass().getName());
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("Could not deserialize the received object.", e);
                }
            }
        } catch (EOFException e) {
            if (running) {
                close();
            }
        } catch (IOException e) {
            logger.error("Error while listening to the socket.", e);
        }
    }

    /** Close the socket. */
    @Override
    public synchronized void close() {
        running = false;
        try {
            getSocket().close();
            logger.debug("Socket closed.");
        } catch (IOException e) {
            logger.error("Error closing the socket.", e);
        }
    }

    protected abstract Socket getSocket();

    /** Called when the connection is ready to send and receive data. */
    protected abstract void connectionEstablished();

    /**
     * Called when a packet is received from the other endpoint of the socket.
     *
     * @param packet the received packet.
     */
    protected abstract void receivePacket(Packet packet);

    /**
     * Send a packet to the other endpoint of the socket.
     * This method shouldn't be used without receiving the {@link #connectionEstablished} callback.
     *
     * @param packet the packet to send.
     */
    public void sendPacket(Packet packet) {
        try {
            out.writeObject(packet);
            logger.trace("Packet sent: {}", packet);
        } catch (IOException e) {
            logger.error("Error sending packet.", e);
        }
    }
}
