package deusto.safebox.common.net;

import com.google.gson.JsonObject;
import deusto.safebox.common.util.Constants;
import deusto.safebox.common.util.JsonData;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Represents an endpoint of the socket connection. */
public abstract class ClientConnection extends Thread implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    private DataOutputStream out;

    protected void listen() {
        try (DataInputStream in = new DataInputStream(getSocket().getInputStream())) {
            out = new DataOutputStream(getSocket().getOutputStream());
            connectionEstablished();

            while (!getSocket().isClosed()) {
                String data = in.readUTF();
                receivePacket(new JsonData(Constants.GSON.fromJson(data, JsonObject.class)));
            }
        } catch (IOException e) {
            logger.error("Error listening to the socket.", e);
        } finally {
            if (getSocket() != null && !getSocket().isClosed()) {
                try {
                    logger.trace("Closing socket endpoint.");
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

    /** Called when the connection is ready to send and receive data. */
    protected abstract void connectionEstablished();

    protected abstract void receivePacket(JsonData packet);

    /**
     * Send a packet to the other endpoint of the socket.
     * This method shouldn't be used without receiving the {@link #connectionEstablished} callback.
     *
     * @param packet the packet in JSON format.
     * @throws IOException if there is an error sending the packet.
     */
    public void sendPacket(JsonObject packet) throws IOException {
        out.writeUTF(packet.toString());
    }
}
