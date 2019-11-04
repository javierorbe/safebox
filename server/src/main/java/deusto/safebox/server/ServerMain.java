package deusto.safebox.server;

import deusto.safebox.common.util.ConfigFile;
import deusto.safebox.common.util.JsonConfig;
import deusto.safebox.server.dao.sql.SqlDaoManager;
import deusto.safebox.server.gui.ServerFrame;
import deusto.safebox.server.net.Server;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {

    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    @SuppressWarnings("SpellCheckingInspection")
    private static final Path KEY_PATH = Path.of("/safeboxkey.jks");
    @SuppressWarnings("SpellCheckingInspection")
    private static final String KEY_PASSWORD = "LYXeAqB4VfjyfVGbrc4J";
    /** Resource name of the config file. */
    private static final String CONFIG_RESOURCE = "config.json";
    /** Path where the config file is extracted. */
    private static final Path CONFIG_FILE = Path.of("config.json");

    private static ConfigFile config;

    public static void main(String[] args) {
        try {
            config = JsonConfig.ofResource(CONFIG_RESOURCE, CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Could not load the config file.", e);
            return;
        }

        int socketPort = config.getInt("socketPort");
        logger.trace("Config socket port: {}", socketPort);
        Server server = new Server(socketPort, KEY_PATH, KEY_PASSWORD);

        SqlDaoManager daoManager = getSqlDaoManager();

        if (args.length > 0 && args[0].equalsIgnoreCase("-gui")) {
            SwingUtilities.invokeLater(() -> new ServerFrame(server, daoManager));
        } else {
            server.start();

            // Wait for user input
            Scanner scanner = new Scanner(System.in);
            scanner.next();

            server.close();
            daoManager.close();
        }
    }

    private static SqlDaoManager getSqlDaoManager() {
        String rdbms = config.getString("rdbms");
        switch (rdbms.toLowerCase()) {
            case "sqlite": {
                String sqliteFilepath = config.getString("sqlite.filepath");
                return SqlDaoManager.ofSqlite(Path.of(sqliteFilepath));
            }

            case "mysql": {
                String host = config.getString("mysql.host");
                int port = config.getInt("mysql.port");
                String database = config.getString("mysql.host");
                String username = config.getString("username");
                String password = config.getString("password");
                return SqlDaoManager.ofMysql(host + ":" + port, database, username, password);
            }

            default: {
                throw new IllegalArgumentException("Invalid RDBMS in config: " + rdbms);
            }
        }
    }
}
