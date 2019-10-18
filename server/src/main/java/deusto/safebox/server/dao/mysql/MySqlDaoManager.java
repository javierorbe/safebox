package deusto.safebox.server.dao.mysql;

import deusto.safebox.server.dao.DaoManager;
import deusto.safebox.server.dao.UserDao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDaoManager implements DaoManager {

    private static final String DATABASE_URL = "jdbc:mysql://%s/%s";

    private final Connection connection;

    private UserDao userDao = null;

    public MySqlDaoManager(String host, String username, String password, String database) throws SQLException {
        connection = DriverManager.getConnection(
                String.format(DATABASE_URL, host, database),
                username,
                password
        );
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new MySqlUserDao(connection);
        }
        return userDao;
    }
}
