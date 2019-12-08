package deusto.safebox.server.dao.sql;

import deusto.safebox.server.User;
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

class SqlUserDao implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(SqlUserDao.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
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
    public CompletableFuture<Boolean> insert(User user) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(insert)) {
                statement.setString(1, user.getId().toString());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.setDate(5, Date.valueOf(user.getCreation()));
                future.complete(statement.executeUpdate() > 0);
            } catch (SQLException e) {
                logger.error("SQL error.", e);
                future.complete(false);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Boolean> update(User user) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(update)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setString(4, user.getId().toString());
                future.complete(statement.executeUpdate() > 0);
            } catch (SQLException e) {
                logger.error("SQL error.", e);
                future.complete(false);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Boolean> delete(User user) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(delete)) {
                statement.setString(1, user.getId().toString());
                future.complete(statement.executeUpdate() > 0);
            } catch (SQLException e) {
                logger.error("SQL error.", e);
                future.complete(false);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Optional<User>> get(UUID userId) {
        CompletableFuture<Optional<User>> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getOne)) {
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
                logger.error("SQL error.", e);
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<List<User>> getAll() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getAll)) {
                List<User> users = new ArrayList<>();
                try (ResultSet set = statement.executeQuery()) {
                    while (set.next()) {
                        users.add(convert(set));
                    }
                }
                future.complete(users);
            } catch (SQLException e) {
                logger.error("SQL error.", e);
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Optional<User>> getByEmail(String email) {
        CompletableFuture<Optional<User>> future = new CompletableFuture<>();

        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getOneEmail)) {
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
                logger.error("SQL error.", e);
                future.completeExceptionally(e);
            }
        });

        return future;
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
