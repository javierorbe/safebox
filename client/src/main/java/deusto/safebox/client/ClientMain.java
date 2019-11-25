package deusto.safebox.client;

import deusto.safebox.client.gui.MainFrame;
import deusto.safebox.client.net.Client;
import deusto.safebox.client.net.PacketHandler;
import deusto.safebox.common.util.ConfigFile;
import deusto.safebox.common.util.JsonConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ClientMain {

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

    /** Program data directory. */
    private static final Path PROGRAM_DIRECTORY = Path.of(getUserDataDirectory(), ".safebox");
    /** Resource name of the config file. */
    private static final String CONFIG_RESOURCE = "config.json";
    /** Path where the config file is extracted inside the program data directory. */
    private static final Path CONFIG_FILE = Path.of("config.json");

    public static void main(String[] args) {
        ConfigFile config;
        try {
            Files.createDirectories(PROGRAM_DIRECTORY);
            config = JsonConfig.ofResource(CONFIG_RESOURCE, PROGRAM_DIRECTORY.resolve(CONFIG_FILE));
        } catch (IOException e) {
            logger.error("Error loading the config file.", e);
            return;
        }

        String hostname = config.getString("server.hostname");
        int port = config.getInt("server.port");

        Client client = new Client(hostname, port);
        client.setPacketReceived(packet -> {
            logger.trace("Received a packet: {}", packet);
            PacketHandler.INSTANCE.fire(packet);
        });

        String lang = config.getString("lang");
        logger.info("Selected language: {}", lang);

        client.start();
        SwingUtilities.invokeLater(() -> new MainFrame(client));
    }

    /**
     * Returns the path to a user dependent directory.
     *
     * <p>For Windows operating systems, it is <code>C:\Users\&lt;username&gt;\AppData\Roaming\</code>.
     * For other operating systems, the user home directory is used.
     *
     * @return the absolute path to the user directory.
     */
    private static String getUserDataDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return System.getenv("AppData");
        } else {
            return System.getProperty("user.home");
        }
    }

    private ClientMain() {
        throw new AssertionError();
    }
}
