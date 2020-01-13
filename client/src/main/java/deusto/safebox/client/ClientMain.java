package deusto.safebox.client;

import deusto.safebox.client.gui.MainFrame;
import deusto.safebox.client.locale.Language;
import deusto.safebox.client.locale.LocaleManager;
import deusto.safebox.client.net.Client;
import deusto.safebox.client.net.PacketHandler;
import deusto.safebox.common.util.ConfigFile;
import deusto.safebox.common.util.JsonConfig;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMain.class);

    /** Program data directory. */
    private static final Path PROGRAM_DIRECTORY = Path.of(getUserDataDirectory(), ".safebox");
    /** Resource name of the config file. */
    private static final String CONFIG_RESOURCE = "config.json";
    /** Path where the config file is extracted inside the program data directory. */
    private static final Path CONFIG_FILE = Path.of("config.json");

    public static ConfigFile CONFIG;

    public static void main(String[] args) {
        try {
            Files.createDirectories(PROGRAM_DIRECTORY);
            CONFIG = JsonConfig.ofResource(CONFIG_RESOURCE, PROGRAM_DIRECTORY.resolve(CONFIG_FILE));
        } catch (IOException e) {
            LOGGER.error("Error loading the config file.", e);
            return;
        }

        String hostname = CONFIG.getString("server.hostname");
        int port = CONFIG.getInt("server.port");

        Client client = new Client(hostname, port);
        client.setPacketReceived(packet -> {
            LOGGER.trace("Received a packet: {}", packet);
            PacketHandler.INSTANCE.fire(packet);
        });

        String langCode = CONFIG.getString("lang");
        LOGGER.info("Selected language: {}", langCode);

        Language language = Language.fromCode(langCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid language code: " + langCode));
        try {
            LocaleManager.loadTranslation(language);
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Error loading language file.", e);
        }

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
