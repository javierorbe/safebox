package deusto.safebox.server.dao.sql;

import deusto.safebox.server.dao.DaoManager;
import deusto.safebox.server.dao.UserDao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQLite implementation of a {@link DaoManager}.
 */
public class SqLiteDaoManager implements DaoManager {

    private static final Logger logger = LoggerFactory.getLogger(SqLiteDaoManager.class);

    private static final String DATABASE_URL = "jdbc:sqlite:%s";

    private final Connection connection;
    private final UserDao userDao;

    /**
     * Creates a connection to a SQLite database.
     *
     * @param path absolute path to the database file location.
     * @throws SQLException if the connection to the database fails.
     */
    public SqLiteDaoManager(String path) throws SQLException {
        connection = DriverManager.getConnection(
                String.format(DATABASE_URL, path)
        );

        logger.info("Connected to SQLite database.");

        userDao = new SqlUserDao(connection);
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing SQLite connection.", e);
        }
    }
}
