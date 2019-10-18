package deusto.safebox.server;

import deusto.safebox.common.util.DataObject;
import deusto.safebox.common.util.JsonData;
import deusto.safebox.server.net.Server;
import java.io.IOException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {

    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    @SuppressWarnings("SpellCheckingInspection")
    private static final String KEY_PATH = "/safeboxkey.jks";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String KEY_PASSWORD = "LYXeAqB4VfjyfVGbrc4J";
    private static final String CONFIG_FILE = "config.json";

    public static void main(String[] args) {
        final DataObject serverConfig;
        try {
            serverConfig = JsonData.getOrExtractResource("/" + CONFIG_FILE, CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Could not load the config file.", e);
            return;
        }

        final int socketPort = serverConfig.getInt("socketPort");
        logger.trace("Config socket port: {}", socketPort);

        Server server = new Server(socketPort, KEY_PATH, KEY_PASSWORD);
        server.start();

        // Wait for user input
        Scanner scanner = new Scanner(System.in);
        scanner.next();

        server.close();
    }
}
