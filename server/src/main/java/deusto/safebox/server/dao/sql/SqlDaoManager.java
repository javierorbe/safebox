package deusto.safebox.server.dao.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import deusto.safebox.server.dao.DaoManager;
import deusto.safebox.server.dao.ItemCollectionDao;
import deusto.safebox.server.dao.UserDao;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link DaoManager} for SQL databases.
 */
public class SqlDaoManager implements DaoManager, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlDaoManager.class);

    private final HikariDataSource dataSource;

    private final UserDao userDao;
    private final ItemCollectionDao itemCollectionDao;

    /**
     * Constructs a {@code SqlDaoManager} for the specified DBMS and configuration.
     *
     * @param database the DBMS
     * @param config the HikariCP configuration for the database connection
     */
    private SqlDaoManager(SqlDatabase database, HikariConfig config) {
        dataSource = new HikariDataSource(config);

        LOGGER.info("Connected to the database.");

        userDao = new SqlUserDao(database, dataSource::getConnection);
        itemCollectionDao = new SqlItemCollectionDao(database, dataSource::getConnection);
    }

    public String getJdbcUrl() {
        return dataSource.getJdbcUrl();
    }

    /** Closes the connection to the database. */
    @Override
    public void close() {
        dataSource.close();
        LOGGER.info("Database connection closed.");
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
     * @param file path to the database file
     * @return a {@code SqlDaoManager} connected to the specified SQLite database
     */
    public static SqlDaoManager ofSqlite(Path file) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(SqlDatabase.SQLITE.getJdbcUrl(), file.toString()));
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        return new SqlDaoManager(SqlDatabase.SQLITE, config);
    }

    /**
     * Returns a {@code SqlDaoManager} connected to the specified MySQL database.
     *
     * @param host server address
     * @param database database name
     * @param username database access username
     * @param password database access password
     * @return a {@code SqlDaoManager} connected to the specified MySQL database
     * @see <a href="https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration" target="_top">MySQL Configuration</a>
     */
    public static SqlDaoManager ofMysql(String host, String database, String username, String password) {
        return ofServerSql(SqlDatabase.MYSQL, host, database, username, password);
    }

    /**
     * Returns a {@code SqlDaoManager} connected to the specified PostgreSQL database.
     *
     * @param host server address
     * @param database database name
     * @param username database access username
     * @param password database access password
     * @return a {@code SqlDaoManager} connected to the specified PostgreSQL database
     */
    public static SqlDaoManager ofPostgreSql(String host, String database, String username, String password) {
        return ofServerSql(SqlDatabase.POSTGRESQL, host, database, username, password);
    }

    private static SqlDaoManager ofServerSql(SqlDatabase database,
                                             String host, String databaseName, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(database.getJdbcUrl(), host, databaseName));
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.setMaximumPoolSize(15);
        return new SqlDaoManager(database, config);
    }
}
