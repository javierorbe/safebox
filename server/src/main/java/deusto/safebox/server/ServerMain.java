package deusto.safebox.server;

import static deusto.safebox.common.util.GuiUtil.runSwing;

import deusto.safebox.common.util.ConfigFile;
import deusto.safebox.common.util.JsonConfig;
import deusto.safebox.server.dao.sql.SqlDaoManager;
import deusto.safebox.server.gui.ServerFrame;
import deusto.safebox.server.net.Server;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    /** Resource name of the config file. */
    private static final String CONFIG_RESOURCE = "config.json";
    /** Path where the config file is extracted. */
    private static final Path CONFIG_FILE = Path.of("config.json");

    public static void main(String[] args) {
        ConfigFile config;
        try {
            config = JsonConfig.ofResource(CONFIG_RESOURCE, CONFIG_FILE);
        } catch (IOException e) {
            LOGGER.error("Could not load the config file.", e);
            return;
        }

        int socketPort = config.getInt("socketPort");
        // TODO: get a key path of the system disk provided in the config
        Path keyPath = getDefaultKeyPath();
        String keyPassword = config.getString("keyPassword");

        SqlDaoManager daoManager = getSqlDaoManager(config);
        Server server = new Server(socketPort, keyPath, keyPassword, daoManager);

        // Open a window if the argument '-gui' is provided.
        if (args.length > 0 && args[0].equalsIgnoreCase("-gui")) {
            runSwing(() -> new ServerFrame(server, daoManager));
        } else {
            server.start();

            // Wait for user input
            Scanner scanner = new Scanner(System.in);
            scanner.next();

            server.close();
            daoManager.close();
        }
    }

    /**
     * Constructs a {@link SqlDaoManager} from the configuration file.
     *
     * @param config the config file.
     * @return a {@code SqlDaoManager} connected to the database provided in the configuration.
     */
    private static SqlDaoManager getSqlDaoManager(ConfigFile config) {
        String dbms = config.getString("dbms");
        switch (dbms.toLowerCase()) {
            case "sqlite": {
                String sqliteFilepath = config.getString("sqlite.filepath");
                return SqlDaoManager.ofSqlite(Path.of(sqliteFilepath));
            }

            case "mysql": {
                String host = config.getString("mysql.host");
                int port = config.getInt("mysql.port");
                String database = config.getString("mysql.database");
                String username = config.getString("mysql.username");
                String password = config.getString("mysql.password");
                return SqlDaoManager.ofMysql(host + ":" + port, database, username, password);
            }

            case "postgresql": {
                String host = config.getString("postgresql.host");
                int port = config.getInt("postgresql.port");
                String database = config.getString("postgresql.database");
                String username = config.getString("postgresql.username");
                String password = config.getString("postgresql.password");
                return SqlDaoManager.ofPostgreSql(host + ":" + port, database, username, password);
            }

            default: {
                throw new IllegalArgumentException("Invalid DBMS in config: " + dbms);
            }
        }
    }

    // TEMP
    private static Path getDefaultKeyPath() {
        try {
            @SuppressWarnings("SpellCheckingInspection")
            URI uri = ServerMain.class.getResource("/safeboxkey.jks").toURI();
            try {
                FileSystems.newFileSystem(uri, Map.of("create", "true"));
            } catch (IllegalArgumentException ignored) {
                // ignored
            }
            return Paths.get(uri);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
