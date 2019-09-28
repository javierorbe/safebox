package deusto.safebox.server;

import deusto.safebox.common.util.ConfigFile;
import deusto.safebox.common.util.JsonConfigFile;
import deusto.safebox.server.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ServerMain {

    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    private static final String KEY_PATH = "/safeboxkey.jks";
    private static final String KEY_PASSWORD = "LYXeAqB4VfjyfVGbrc4J";
    private static final String CONFIG_FILE = "config.json";

    private static ConfigFile serverConfig;

    public static void main(String[] args) {
        try {
            loadConfig();
        } catch (FileNotFoundException e) {
            logger.error("Could not read server config file.", e);
            return;
        }

        int socketPort = serverConfig.getInt("socketPort");
        logger.info("Socket port: {}", socketPort);

        Server server = new Server(socketPort, KEY_PATH, KEY_PASSWORD);
        server.start();
    }

    private static void loadConfig() throws FileNotFoundException {
        if (!new File(CONFIG_FILE).exists()) {
            logger.info("Config file doesn't exist, creating it...");
            try {
                extractFile(CONFIG_FILE);
            } catch (IOException e) {
                logger.error("Could not create server config file.", e);
                return;
            }
        }

        serverConfig = new JsonConfigFile(CONFIG_FILE);
    }

    /** Extract a resource file to disk. */
    private static void extractFile(String filename) throws IOException {
        InputStream ddlStream = ServerMain.class.getResourceAsStream("/" + CONFIG_FILE);

        try (FileOutputStream fos = new FileOutputStream(filename)){
            byte[] buf = new byte[2048];
            int r;
            while((r = ddlStream.read(buf)) != -1) {
                fos.write(buf, 0, r);
            }
        }
    }
}
