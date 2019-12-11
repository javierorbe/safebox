package deusto.safebox.server.dao.sql;

import static deusto.safebox.server.dao.sql.SqlItemCollectionDao.ItemCollectionStatement.DELETE;
import static deusto.safebox.server.dao.sql.SqlItemCollectionDao.ItemCollectionStatement.GET_ONE;
import static deusto.safebox.server.dao.sql.SqlItemCollectionDao.ItemCollectionStatement.INSERT_ONE_ITEM;
import static deusto.safebox.server.dao.sql.SqlUtil.transaction;

import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import deusto.safebox.server.ItemCollection;
import deusto.safebox.server.dao.DaoException;
import deusto.safebox.server.dao.ItemCollectionDao;
import deusto.safebox.server.util.CheckedSupplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link ItemCollectionDao} for SQL databases.
 */
class SqlItemCollectionDao implements ItemCollectionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlItemCollectionDao.class);

    private final CheckedSupplier<Connection, SQLException> connectionSupplier;
    private final SqlDatabase database;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    SqlItemCollectionDao(SqlDatabase database, CheckedSupplier<Connection, SQLException> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        this.database = database;
    }

    @Override
    public CompletableFuture<Boolean> insert(ItemCollection collection) {
        return CompletableFuture.supplyAsync(() -> {
            AtomicBoolean insertResult = new AtomicBoolean(false);
            boolean transactionResult = false;
            try (Connection connection = connectionSupplier.get()) {
                transactionResult = transaction(connection, () -> {
                    // First all the stored items owned by the user are deleted.
                    // This has to be done because we don't take into account which items have been deleted from the
                    // collection that is going to be inserted.
                    try (PreparedStatement statement = connection.prepareStatement(getStmt(DELETE))) {
                        statement.setString(1, collection.getUserId().toString());
                        statement.executeUpdate();
                    }
                    // Insert all the items in a batch.
                    try (PreparedStatement statement = connection.prepareStatement(getStmt(INSERT_ONE_ITEM))) {
                        for (ItemData item : collection.getItems()) {
                            statement.setString(1, item.getId().toString());
                            statement.setString(2, collection.getUserId().toString());
                            statement.setByte(3, item.getType().getId());
                            statement.setString(4, item.getEncryptedData());
                            statement.setTimestamp(5, Timestamp.valueOf(item.getCreated()));
                            statement.setTimestamp(6, Timestamp.valueOf(item.getLastModified()));
                            statement.addBatch();
                        }
                        int[] results = statement.executeBatch();
                        // Test that all the insertions have been successful.
                        insertResult.set(Arrays.stream(results).allMatch(e -> e > 0));
                    }
                });
            } catch (SQLException e) {
                LOGGER.error("Error getting a connection.", e);
            }
            return insertResult.get() && transactionResult;
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> update(ItemCollection collection) {
        // We don't distinguish between an item collection insertion and an update, so use the insert method.
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Boolean> delete(ItemCollection collection) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getStmt(DELETE))) {
                statement.setString(1, collection.getUserId().toString());
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                LOGGER.error("SQL error.", e);
                return false;
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Optional<ItemCollection>> get(UUID userId) {
        CompletableFuture<Optional<ItemCollection>> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try (Connection connection = connectionSupplier.get();
                 PreparedStatement statement = connection.prepareStatement(getStmt(GET_ONE))) {
                statement.setString(1, userId.toString());

                Collection<ItemData> items = new HashSet<>();
                try (ResultSet set = statement.executeQuery()) {
                    while (set.next()) {
                        items.add(convert(set));
                    }
                }
                future.complete(Optional.of(new ItemCollection(userId, items)));
            } catch (SQLException e) {
                LOGGER.error("SQL error.", e);
                future.completeExceptionally(new DaoException(e));
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<List<ItemCollection>> getAll() {
        // Not used.
        throw new UnsupportedOperationException();
    }

    private String getStmt(ItemCollectionStatement statement) {
        return statement.get(database);
    }

    private static ItemData convert(ResultSet set) throws SQLException {
        UUID id = UUID.fromString(set.getString("id"));
        ItemType type  = ItemType.fromId(set.getByte("type"));
        String data = set.getString("data");
        LocalDateTime created = set.getTimestamp("creation").toLocalDateTime();
        LocalDateTime lastModified = set.getTimestamp("last_modified").toLocalDateTime();
        return new ItemData(id, type, data, created, lastModified);
    }

    enum ItemCollectionStatement implements SqlStatement {
        INSERT_ONE_ITEM(
                "INSERT INTO sb_item (id, user_id, type, data, creation, last_modified) VALUES (?, ?, ?, ?, ?, ?)"),
        DELETE("DELETE FROM sb_item WHERE user_id=?"),
        GET_ONE("SELECT id, type, data, creation, last_modified FROM sb_item WHERE user_id=?"),
        ;

        private final String statement;

        ItemCollectionStatement(String statement) {
            this.statement = statement;
        }

        @Override
        public String get(SqlDatabase database) {
            return statement;
        }
    }
}
