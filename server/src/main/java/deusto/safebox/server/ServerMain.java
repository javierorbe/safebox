package deusto.safebox.server;

import deusto.safebox.common.util.ConfigFile;
import deusto.safebox.common.util.JsonConfig;
import deusto.safebox.server.dao.sql.MySqlDaoManager;
import deusto.safebox.server.dao.sql.SqLiteDaoManager;
import deusto.safebox.server.dao.sql.SqlDaoManager;
import deusto.safebox.server.gui.ServerFrame;
import deusto.safebox.server.net.Server;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.SwingUtilities;
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
        ConfigFile config;
        try {
            // The file is extracted in the same directory as the application executable.
            config = JsonConfig.getOrExtractResource("/" + CONFIG_FILE, CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Could not load the config file.", e);
            return;
        }

        int socketPort = config.getInt("socketPort");
        logger.trace("Config socket port: {}", socketPort);
        Server server = new Server(socketPort, KEY_PATH, KEY_PASSWORD);

        SqlDaoManager daoManager;
        try {
            daoManager = getSqlDaoManager(config);
        } catch (SQLException e) {
            logger.error("Could not connect to the database.", e);
            return;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("-gui")) {
            SwingUtilities.invokeLater(() -> new ServerFrame(server, daoManager));
        } else {
            server.start();

            // Wait for user input
            Scanner scanner = new Scanner(System.in);
            scanner.next();

            daoManager.close();
            server.close();
        }
    }

    private static SqlDaoManager getSqlDaoManager(ConfigFile config) throws SQLException {
        String rdbms = config.getString("rdbms");
        switch (rdbms.toLowerCase()) {
            case "sqlite": {
                String sqliteFilepath = config.getString("sqlite.filepath");
                return new SqLiteDaoManager(sqliteFilepath);
            }

            case "mysql": {
                String host = config.getString("mysql.host");
                int port = config.getInt("mysql.port");
                String database = config.getString("mysql.host");
                String username = config.getString("username");
                String password = config.getString("password");
                return new MySqlDaoManager(host + ":" + port, database, username, password);
            }

            default: {
                throw new IllegalArgumentException("Invalid RDBMS in config: " + rdbms);
            }
        }
    }
}
