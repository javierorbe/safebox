package deusto.safebox.server.dao.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import deusto.safebox.server.dao.DaoManager;
import deusto.safebox.server.dao.ItemCollectionDao;
import deusto.safebox.server.dao.UserDao;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlDaoManager implements DaoManager, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(SqlDaoManager.class);

    private static final String SQLITE_URL = "jdbc:sqlite:%s";
    private static final String MYSQL_URL = "jdbc:mysql://%s/%s";

    private final HikariDataSource dataSource;

    private final UserDao userDao;
    private final ItemCollectionDao itemCollectionDao;

    private SqlDaoManager(HikariConfig config) {
        dataSource = new HikariDataSource(config);

        logger.info("Connected to the database.");

        Supplier<Optional<Connection>> connectionSupplier = () -> {
            try {
                Connection connection = dataSource.getConnection();
                return Optional.of(connection);
            } catch (SQLException e) {
                logger.error("Error getting a connection pool.", e);
                return Optional.empty();
            }
        };

        userDao = new SqlUserDao(connectionSupplier);
        itemCollectionDao = new SqlItemCollectionDao(connectionSupplier);
    }

    public DatabaseMetaData getDatabaseMetadata() throws SQLException {
        return dataSource.getConnection().getMetaData();
    }

    @Override
    public void close() {
        dataSource.close();
        logger.info("Database connection closed.");
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public ItemCollectionDao getItemCollectionDao() {
        return itemCollectionDao;
    }

    public static SqlDaoManager ofSqlite(String path) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(SQLITE_URL, path));
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        return new SqlDaoManager(config);
    }

    /**
     * Creates a connection to a MySQL database.
     *
     * @param host server address.
     * @param database database name.
     * @param username database access username.
     * @param password database access password.
     */
    public static SqlDaoManager ofMysql(String host, String database, String username, String password) {
        // TODO: use this config https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(MYSQL_URL, host, database));
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        return new SqlDaoManager(config);
    }
}
