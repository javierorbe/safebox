package deusto.safebox.server.dao.sql;

import deusto.safebox.server.dao.DaoManager;
import deusto.safebox.server.dao.UserDao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MySQL implementation of a {@link DaoManager}.
 */
public class MySqlDaoManager implements DaoManager {

    private static final Logger logger = LoggerFactory.getLogger(MySqlDaoManager.class);

    private static final String DATABASE_URL = "jdbc:mysql://%s/%s";

    private final Connection connection;
    private final UserDao userDao;

    /**
     * Creates a connection to a MySQL database.
     *
     * @param host server address.
     * @param database database name.
     * @param username database access username.
     * @param password database access password.
     * @throws SQLException if the connection to the database fails.
     */
    public MySqlDaoManager(String host, String database, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(
                String.format(DATABASE_URL, host, database),
                username,
                password
        );

        logger.info("Connected to MySQL database.");

        userDao = new SqlUserDao(connection);
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing MySQL connection.", e);
        }
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }
}
