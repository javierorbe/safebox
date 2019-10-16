package deusto.safebox.client;

import deusto.safebox.common.util.JsonData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.SwingUtilities;

public class ClientMain {

    // private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

    /** Program data folder name. */
    private static final String FOLDER_NAME = ".safebox";
    private static final String CONFIG_FILE = "config.json";

    public static void main(String[] args) {
        /*
        DataObject config = getClientConfig();

        String hostname = config.getString("server.hostname");
        int port = config.getInt("server.port");

        Client client = new Client(hostname, port);
        */

        SwingUtilities.invokeLater(() -> {
            // TODO: create main window
        });
    }

    /**
     * Returns the contents of the client file.
     *
     * <p>The program data directory is created if it doesn't exist.
     *
     * @return a {@link JsonData} with the contents of the config file.
     * @throws RuntimeException if the data directory cannot be created or the config file cannot be read.
     * @throws IllegalArgumentException if the path to the data directory doesn't contain a directory.
     */
    private static JsonData getClientConfig() {
        File programDirectory = Path.of(getUserDataDirectory(), FOLDER_NAME).toFile();
        if (!programDirectory.exists()) {
            if (!programDirectory.mkdir()) {
                throw new RuntimeException("Could not create the data directory " + programDirectory.toString());
            }
        } else if (!programDirectory.isDirectory()) {
            throw new IllegalArgumentException("The path is not a directory.");
        }

        try {
            return JsonData.getOrExtractResource(
                    "/" + CONFIG_FILE,
                    Path.of(programDirectory.getPath(), CONFIG_FILE).toString()
            );
        } catch (IOException e) {
            throw new RuntimeException("Could not load the config file.", e);
        }
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
}
