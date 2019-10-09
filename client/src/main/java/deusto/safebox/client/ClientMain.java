package deusto.safebox.client;

import com.google.gson.JsonObject;
import deusto.safebox.client.gui.MainWindow;
import deusto.safebox.client.net.Client;
import deusto.safebox.common.util.Constants;
import deusto.safebox.common.util.DataObject;
import deusto.safebox.common.util.JsonData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

    private static final String DATA_DIRECTORY = ".safebox";
    private static final String CONFIG_FILE = "config.json";

    public static void main(String[] args) {
        DataObject serverConfig = loadConfig();

        final String hostname = serverConfig.getString("server.hostname");
        final int port = serverConfig.getInt("server.port");

        Client client = new Client(hostname, port);
        SwingUtilities.invokeLater(() -> new MainWindow() {
            @Override
            protected void connect() {
                logger.trace("Connecting to the server...");
                client.start();
            }

            @Override
            protected void send() {
                new Thread(() -> {
                    try {
                        logger.trace("Sending a packet...");
                        client.sendPacket(Constants.GSON.fromJson(
                                "{ \"data1\": 1337, \"data2\": 42 }",
                                JsonObject.class
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }

    private static DataObject loadConfig() {
        final String dataDirectoryPath = Path.of(getAppDataDirectory(), DATA_DIRECTORY).toString();
        // Create the program data directory if it doesn't exist.
        if (!new File(dataDirectoryPath).exists()) {
            if (!new File(dataDirectoryPath).mkdir()) {
                throw new RuntimeException("Could not create the data directory.");
            }
        }

        try {
            return JsonData.getOrExtractResource(
                    "/" + CONFIG_FILE,
                    Path.of(dataDirectoryPath, CONFIG_FILE).toString()
            );
        } catch (IOException e) {
            throw new RuntimeException("Could not load the config file.", e);
        }
    }

    private static String getAppDataDirectory() {
        String os = System.getProperty("os.name").toUpperCase();
        if (os.contains("WIN")) {
            return System.getenv("AppData");
        } else {
            return System.getProperty("user.home");
        }
    }
}
