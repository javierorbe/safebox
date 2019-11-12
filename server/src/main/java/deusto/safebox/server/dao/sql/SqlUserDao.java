package deusto.safebox.server.dao.sql;

import deusto.safebox.server.User;
import deusto.safebox.server.dao.DaoException;
import deusto.safebox.server.dao.UserDao;
import deusto.safebox.server.util.CheckedSupplier;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class SqlUserDao implements UserDao {

    private final CheckedSupplier<Connection, SQLException> connectionSupplier;

    private final String insert;
    private final String update;
    private final String delete;
    private final String getOne;
    private final String getAll;
    private final String getOneEmail;

    SqlUserDao(SqlDatabase database, CheckedSupplier<Connection, SQLException> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;

        insert = UserStatement.INSERT.get(database);
        update = UserStatement.UPDATE.get(database);
        delete = UserStatement.DELETE.get(database);
        getOne = UserStatement.GET_ONE.get(database);
        getAll = UserStatement.GET_ALL.get(database);
        getOneEmail = UserStatement.GET_ONE_EMAIL.get(database);
    }

    @Override
    public boolean insert(User user) throws DaoException {
        try (Connection connection = connectionSupplier.get();
                PreparedStatement statement = connection.prepareStatement(insert)) {
            statement.setString(1, user.getId().toString());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setDate(5, Date.valueOf(user.getCreation()));
            statement.setString(6, user.getName());
            statement.setString(7, user.getEmail());
            statement.setString(8, user.getPassword());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public boolean update(User user) throws DaoException {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(update)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getId().toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public boolean delete(User user) throws DaoException {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setString(1, user.getId().toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public Optional<User> get(UUID userId) throws DaoException {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(getOne)) {
            statement.setString(1, userId.toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    User user = convert(set);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public List<User> getAll() throws DaoException {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(getAll)) {
            List<User> users = new ArrayList<>();
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    users.add(convert(set));
                }
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public Optional<User> getByEmail(String email) throws DaoException {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(getOneEmail)) {
            statement.setString(1, email);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    User user = convert(set);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    private static User convert(ResultSet set) throws SQLException {
        UUID id = UUID.fromString(set.getString("id"));
        String name = set.getString("name");
        String email = set.getString("email");
        String password = set.getString("password");
        LocalDate creation = set.getDate("creation").toLocalDate();
        return new User(id, name, email, password, creation);
    }

    private enum UserStatement implements SqlStatement {
        INSERT("INSERT INTO user (id, name, email, password, creation) VALUES (?, ?, ?, ?, ?)"),
        UPDATE("UPDATE user SET name=?, email=?, password=? WHERE id=?"),
        DELETE("DELETE FROM user WHERE id=?"),
        GET_ONE("SELECT id, name, email, password, creation FROM user WHERE id=?"),
        GET_ALL("SELECT id, name, email, password, creation FROM user"),
        GET_ONE_EMAIL("SELECT id, name, email, password, creation FROM user WHERE email=?")
        ;

        private final String sqliteStmt;
        private final String mysqlStmt;

        UserStatement(String sqliteStmt, String mysqlStmt) {
            this.sqliteStmt = sqliteStmt;
            this.mysqlStmt = mysqlStmt;
        }

        UserStatement(String genericStmt) {
            this(genericStmt, genericStmt);
        }

        @Override
        public String get(SqlDatabase database) {
            if (database == SqlDatabase.SQLITE) {
                return sqliteStmt;
            } else if (database == SqlDatabase.MYSQL) {
                return mysqlStmt;
            }
            throw new IllegalArgumentException("Unknown database (" + database + ")");
        }
    }
}
