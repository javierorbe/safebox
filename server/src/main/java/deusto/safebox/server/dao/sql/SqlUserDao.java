package deusto.safebox.server.dao.sql;

import deusto.safebox.server.User;
import deusto.safebox.server.dao.DaoException;
import deusto.safebox.server.dao.UserDao;
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

    private static final String INSERT
            = "INSERT INTO user (id, name, email, password, creation) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE
            = "UPDATE user SET name=?, email=? WHERE id=?";
    private static final String DELETE
            = "DELETE FROM user WHERE id=?";
    private static final String GET_ONE
            = "SELECT id, name, email, password, creation FROM user WHERE id=?";
    private static final String GET_ALL
            = "SELECT id, name, email, password FROM user";
    private static final String GET_ONE_EMAIL
            = "SELECT id, name, email, password FROM user WHERE email=?";

    private final Connection connection;

    SqlUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(User user) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, user.getId().toString());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setDate(5, Date.valueOf(user.getCreation()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public boolean update(User user) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getId().toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public boolean delete(User user) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setString(1, user.getId().toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public Optional<User> get(UUID userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(GET_ONE)) {
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
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL)) {
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
        try (PreparedStatement statement = connection.prepareStatement(GET_ONE_EMAIL)) {
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
}
