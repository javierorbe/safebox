package deusto.safebox.server.dao.sql;

import deusto.safebox.common.ItemType;
import deusto.safebox.common.net.ItemPacketData;
import deusto.safebox.server.ItemCollection;
import deusto.safebox.server.dao.DaoException;
import deusto.safebox.server.dao.ItemCollectionDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlItemCollectionDao implements ItemCollectionDao {

    private static final Logger logger = LoggerFactory.getLogger(SqlItemCollectionDao.class);

    private static final String INSERT_ONE_ITEM
            = "INSERT INTO item (id, user_id, type, data, creation, last_modified) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ONE_ITEM
            = "UPDATE item SET data=?, last_modified=? WHERE id=?";
    private static final String DELETE
            = "DELETE FROM item WHERE user_id=?";
    private static final String GET_ONE
            = "SELECT id, type, data, creation, last_modified FROM item WHERE user_id=?";

    private final Supplier<Optional<Connection>> connectionSupplier;

    SqlItemCollectionDao(Supplier<Optional<Connection>> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    private Connection getConnection() throws DaoException {
        return connectionSupplier.get()
                .orElseThrow(() -> new DaoException("Could not get a connection from the pool."));
    }

    @Override
    public boolean insert(ItemCollection collection) throws DaoException {
        Connection connection = getConnection();

        try (PreparedStatement statement = connection.prepareStatement(INSERT_ONE_ITEM)) {
            connection.setAutoCommit(false);

            for (ItemPacketData item : collection.getItems()) {
                statement.setString(1, item.getId().toString());
                statement.setString(2, collection.getUserId().toString());
                statement.setByte(3, item.getType().getId());
                statement.setString(4, item.getEncryptedData());
                statement.setTimestamp(5, Timestamp.valueOf(item.getCreated()));
                statement.setTimestamp(6, Timestamp.valueOf(item.getLastModified()));
                statement.addBatch();
            }

            int[] results = statement.executeBatch();
            connection.commit();
            return Arrays.stream(results).allMatch(result -> result > 0);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error in transaction rollback.", rollbackEx);
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error in transaction rollback.", e);
            }
        }
    }

    @Override
    public boolean update(ItemCollection collection) throws DaoException {
        Connection connection = getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ONE_ITEM)) {
            connection.setAutoCommit(false);

            for (ItemPacketData item : collection.getItems()) {
                statement.setString(1, item.getEncryptedData());
                statement.setTimestamp(2, Timestamp.valueOf(item.getLastModified()));
                statement.setString(3, item.getId().toString());
                statement.addBatch();
            }

            int[] results = statement.executeBatch();
            connection.commit();
            return Arrays.stream(results).allMatch(result -> result > 0);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error in transaction rollback.", rollbackEx);
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error in transaction rollback.", e);
            }
        }
    }

    @Override
    public boolean delete(ItemCollection collection) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE)) {
            statement.setString(1, collection.getUserId().toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public Optional<ItemCollection> get(UUID userId) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(GET_ONE)) {
            statement.setString(1, userId.toString());

            List<ItemPacketData> items = new ArrayList<>();
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
        // TODO
        throw new UnsupportedOperationException();
    }

    private static ItemPacketData convert(ResultSet set) throws SQLException {
        UUID id = UUID.fromString(set.getString("id"));
        ItemType type  = ItemType.fromId(set.getByte("type"));
        String data = set.getString("data");
        LocalDateTime created = set.getTimestamp("creation").toLocalDateTime();
        LocalDateTime lastModified = set.getTimestamp("last_modified").toLocalDateTime();
        return new ItemPacketData(id, type, data, created, lastModified);
    }
}
