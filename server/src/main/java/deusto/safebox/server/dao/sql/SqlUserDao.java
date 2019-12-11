package deusto.safebox.server.dao.sql;

import static deusto.safebox.server.dao.sql.SqlUserDao.UserStatement.DELETE;
import static deusto.safebox.server.dao.sql.SqlUserDao.UserStatement.GET_ALL;
import static deusto.safebox.server.dao.sql.SqlUserDao.UserStatement.GET_ONE;
import static deusto.safebox.server.dao.sql.SqlUserDao.UserStatement.GET_ONE_EMAIL;
import static deusto.safebox.server.dao.sql.SqlUserDao.UserStatement.INSERT;
import static deusto.safebox.server.dao.sql.SqlUserDao.UserStatement.UPDATE;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link UserDao} for SQL databases.
 */
class SqlUserDao implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlUserDao.class);

    private final CheckedSupplier<Connection, SQLException> connectionSupplier;
    private final SqlDatabase database;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    SqlUserDao(SqlDatabase database, CheckedSupplier<Connection, SQLException> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        this.database = database;
    }

    @Override
    public CompletableFuture<Boolean> insert(User user) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getStmt(INSERT))) {
                statement.setString(1, user.getId().toString());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.setDate(5, Date.valueOf(user.getCreation()));
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                LOGGER.error("SQL error.", e);
                return false;
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> update(User user) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getStmt(UPDATE))) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setString(4, user.getId().toString());
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                LOGGER.error("SQL error.", e);
                return false;
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> delete(User user) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getStmt(DELETE))) {
                statement.setString(1, user.getId().toString());
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                LOGGER.error("SQL error.", e);
                return false;
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<User>> get(UUID userId) {
        CompletableFuture<Optional<User>> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getStmt(GET_ONE))) {
                statement.setString(1, userId.toString());
                try (ResultSet set = statement.executeQuery()) {
                    if (set.next()) {
                        User user = convert(set);
                        future.complete(Optional.of(user));
                    } else {
                        future.complete(Optional.empty());
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("SQL error.", e);
                future.completeExceptionally(new DaoException(e));
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<List<User>> getAll() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getStmt(GET_ALL))) {
                List<User> users = new ArrayList<>();
                try (ResultSet set = statement.executeQuery()) {
                    while (set.next()) {
                        users.add(convert(set));
                    }
                }
                future.complete(users);
            } catch (SQLException e) {
                LOGGER.error("SQL error.", e);
                future.completeExceptionally(new DaoException(e));
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Optional<User>> getByEmail(String email) {
        CompletableFuture<Optional<User>> future = new CompletableFuture<>();

        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getStmt(GET_ONE_EMAIL))) {
                statement.setString(1, email);
                try (ResultSet set = statement.executeQuery()) {
                    if (set.next()) {
                        User user = convert(set);
                        future.complete(Optional.of(user));
                    } else {
                        future.complete(Optional.empty());
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("SQL error.", e);
                future.completeExceptionally(new DaoException(e));
            }
        });

        return future;
    }

    private String getStmt(UserStatement statement) {
        return statement.get(database);
    }

    private static User convert(ResultSet set) throws SQLException {
        UUID id = UUID.fromString(set.getString("id"));
        String name = set.getString("name");
        String email = set.getString("email");
        String password = set.getString("password");
        LocalDate creation = set.getDate("creation").toLocalDate();
        return new User(id, name, email, password, creation);
    }

    enum UserStatement implements SqlStatement {
        INSERT("INSERT INTO sb_user (id, name, email, password, creation) VALUES (?, ?, ?, ?, ?)"),
        UPDATE("UPDATE sb_user SET name=?, email=?, password=? WHERE id=?"),
        DELETE("DELETE FROM sb_user WHERE id=?"),
        GET_ONE("SELECT id, name, email, password, creation FROM sb_user WHERE id=?"),
        GET_ALL("SELECT id, name, email, password, creation FROM sb_user"),
        GET_ONE_EMAIL("SELECT id, name, email, password, creation FROM sb_user WHERE email=?")
        ;

        private final String statement;

        UserStatement(String statement) {
            this.statement = statement;
        }

        @Override
        public String get(SqlDatabase database) {
            return statement;
        }
    }
}
