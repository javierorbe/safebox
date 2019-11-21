package deusto.safebox.server.dao.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import deusto.safebox.server.dao.DaoManager;
import deusto.safebox.server.dao.ItemCollectionDao;
import deusto.safebox.server.dao.UserDao;
import java.nio.file.Path;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link DaoManager} for SQL databases.
 */
public class SqlDaoManager implements DaoManager, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(SqlDaoManager.class);

    private static final String SQLITE_URL = "jdbc:sqlite:%s";
    private static final String MYSQL_URL = "jdbc:mysql://%s/%s";

    private final HikariDataSource dataSource;

    private final UserDao userDao;
    private final ItemCollectionDao itemCollectionDao;

    /**
     * Constructs a {@code SqlDaoManager} for the specified DBMS and configuration.
     *
     * @param database the DBMS.
     * @param config the Hikari configuration for the database connection.
     */
    private SqlDaoManager(SqlDatabase database, HikariConfig config) {
        dataSource = new HikariDataSource(config);

        logger.info("Connected to the database.");

        userDao = new SqlUserDao(database, dataSource::getConnection);
        itemCollectionDao = new SqlItemCollectionDao(database, dataSource::getConnection);
    }

    public DatabaseMetaData getDatabaseMetadata() throws SQLException {
        return dataSource.getConnection().getMetaData();
    }

    /** Closes the connection to the database. */
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

    /**
     * Returns a {@code SqlDaoManager} connected to the specified SQLite database.
     *
     * @param file path to the database file.
     * @return a {@code SqlDaoManager} connected to the specified SQLite database.
     */
    public static SqlDaoManager ofSqlite(Path file) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(SQLITE_URL, file.toString()));
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        return new SqlDaoManager(SqlDatabase.SQLITE, config);
    }

    /**
     * Returns a {@code SqlDaoManager} connected to the specified MySQL database.
     *
     * @param host server address.
     * @param database database name.
     * @param username database access username.
     * @param password database access password.
     * @return a {@code SqlDaoManager} connected to the specified MySQL database.
     * @see <a href="https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration" target="_top">MySQL Configuration</a>
     */
    public static SqlDaoManager ofMysql(String host, String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(MYSQL_URL, host, database));
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        return new SqlDaoManager(SqlDatabase.MYSQL, config);
    }
}
