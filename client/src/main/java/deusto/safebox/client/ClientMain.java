package deusto.safebox.client;

import deusto.safebox.client.gui.MainWindow;
import deusto.safebox.client.net.Client;
import java.io.IOException;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 6819;

    public static void main(String[] args) {
        Client client = new Client(HOSTNAME, PORT);
        SwingUtilities.invokeLater(() -> new MainWindow() {
            @Override
            protected void connect() {
                logger.trace("Connecting to the server...");
                client.start();
            }

            @Override
            protected void send() {
                logger.trace("Sending a packet...");
                new Thread(() -> {
                    try {
                        client.sendPacket(new byte[] {13, 37});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
}
