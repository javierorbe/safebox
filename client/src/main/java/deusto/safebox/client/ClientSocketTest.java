package deusto.safebox.client;

import deusto.safebox.client.net.Client;
import deusto.safebox.common.net.packet.DisconnectPacket;
import deusto.safebox.common.net.packet.TestPacket;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TEMP
public class ClientSocketTest {

    private static final Logger logger = LoggerFactory.getLogger(ClientSocketTest.class);

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 6819;

    public static void main(String[] args) {
        Client client = new Client(HOSTNAME, PORT);
        SwingUtilities.invokeLater(() -> new SocketTestWindow() {
            @Override
            protected void connect() {
                logger.trace("Connecting to the server...");
                client.start();
            }

            @Override
            protected void send() {
                new Thread(() -> {
                    logger.trace("Sending a packet...");
                    client.sendPacket(new TestPacket("this is a test"));
                }).start();
            }

            @Override
            protected void close() {
                if (client.isConnected()) {
                    client.sendPacket(new DisconnectPacket());
                }
            }
        });
    }

    private abstract static class SocketTestWindow extends JFrame {

        SocketTestWindow() {
            super("Socket Test Client");

            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(480, 320);

            JPanel panel = new JPanel();

            JButton connectBtn = new JButton("Connect");
            JButton sendBtn = new JButton("Send");

            connectBtn.addActionListener(e -> connect());
            sendBtn.addActionListener(e -> send());

            panel.add(connectBtn);
            panel.add(sendBtn);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    close();
                }
            });

            getContentPane().add(panel);

            setVisible(true);
        }

        protected abstract void connect();

        protected abstract void send();

        protected abstract void close();
    }
}
