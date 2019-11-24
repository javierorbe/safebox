package deusto.safebox.server.dao.sql;

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
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SqlItemCollectionDao implements ItemCollectionDao {

    private static final Logger logger = LoggerFactory.getLogger(SqlItemCollectionDao.class);

    private final CheckedSupplier<Connection, SQLException> connectionSupplier;

    private final String insertOneItem;
    private final String delete;
    private final String getOne;

    SqlItemCollectionDao(SqlDatabase database, CheckedSupplier<Connection, SQLException> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;

        insertOneItem = ItemCollectionStatement.INSERT_ONE_ITEM.get(database);
        delete = ItemCollectionStatement.DELETE.get(database);
        getOne = ItemCollectionStatement.GET_ONE.get(database);
    }

    /**
     * Insert a collection of items.
     * First, all the items owned by the user are deleted. This is because we don't register which
     * items have been deleted from the previously stored collection. Then, all the new items are inserted in a batch.
     */
    @Override
    public boolean insert(ItemCollection collection) {
        AtomicBoolean insertResult = new AtomicBoolean(false);
        boolean transactionResult = false;
        try (Connection connection = connectionSupplier.get()) {
            transactionResult = transaction(connection, () -> {
                try (PreparedStatement statement = connection.prepareStatement(delete)) {
                    statement.setString(1, collection.getUserId().toString());
                }

                try (PreparedStatement statement = connection.prepareStatement(insertOneItem)) {
                    for (ItemData item : collection.getItems()) {
                        statement.setString(1, item.getId().toString());
                        statement.setString(2, collection.getUserId().toString());
                        statement.setByte(3, item.getType().getId());
                        statement.setString(4, item.getEncryptedData());
                        statement.setTimestamp(5, Timestamp.valueOf(item.getCreated()));
                        statement.setTimestamp(6, Timestamp.valueOf(item.getLastModified()));
                        statement.setString(7, item.getEncryptedData());
                        statement.setTimestamp(8, Timestamp.valueOf(item.getLastModified()));
                        statement.addBatch();
                    }
                    int[] results = statement.executeBatch();
                    insertResult.set(Arrays.stream(results).allMatch(e -> e > 0));
                }
            });
        } catch (SQLException e) {
            logger.error("Error getting a connection.", e);
        }
        return insertResult.get() && transactionResult;
    }

    @Override
    public boolean update(ItemCollection collection) {
        // We don't distinguish between an item collection insertion and an update, so use the insert method.
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(ItemCollection collection) throws DaoException {
        try (Connection connection = connectionSupplier.get();
                PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setString(1, collection.getUserId().toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public Optional<ItemCollection> get(UUID userId) throws DaoException {
        try (Connection connection = connectionSupplier.get();
                PreparedStatement statement = connection.prepareStatement(getOne)) {
            statement.setString(1, userId.toString());

            Collection<ItemData> items = new HashSet<>();
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    items.add(convert(set));
                }
            }
            return Optional.of(new ItemCollection(userId, items));
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public List<ItemCollection> getAll() {
        throw new UnsupportedOperationException();
    }

    private static ItemData convert(ResultSet set) throws SQLException {
        UUID id = UUID.fromString(set.getString("id"));
        ItemType type  = ItemType.fromId(set.getByte("type"));
        String data = set.getString("data");
        LocalDateTime created = set.getTimestamp("creation").toLocalDateTime();
        LocalDateTime lastModified = set.getTimestamp("last_modified").toLocalDateTime();
        return new ItemData(id, type, data, created, lastModified);
    }

    private enum ItemCollectionStatement implements SqlStatement {
        // TODO: if we finally decide that we are deleting all the
        //  items before inserting new ones, remove the update part of the statement.
        INSERT_ONE_ITEM(
            "INSERT INTO sb_item (id, user_id, type, data, creation, last_modified) "
                    + "VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT(id) DO UPDATE SET data=?, last_modified=?",
            "INSERT INTO sb_item (id, user_id, type, data, creation, last_modified) "
                    + "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE data=?, last_modified=?"
        ),
        DELETE("DELETE FROM sb_item WHERE user_id=?"),
        GET_ONE("SELECT id, type, data, creation, last_modified FROM sb_item WHERE user_id=?"),
        ;

        private final String sqliteStmt;
        private final String mysqlStmt;

        ItemCollectionStatement(String sqliteStmt, String mysqlStmt) {
            this.sqliteStmt = sqliteStmt;
            this.mysqlStmt = mysqlStmt;
        }

        ItemCollectionStatement(String genericStmt) {
            this(genericStmt, genericStmt);
        }

        @Override
        public String get(SqlDatabase database) {
            if (database == SqlDatabase.SQLITE) {
                return sqliteStmt;
            } else {
                return mysqlStmt;
            }
        }
    }
}
