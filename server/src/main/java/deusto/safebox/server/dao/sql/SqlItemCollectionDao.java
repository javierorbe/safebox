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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SqlItemCollectionDao implements ItemCollectionDao {

    private static final String INSERT
            = "INSERT INTO item (id, type, data, created, lastModified) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE
            = "UPDATE item SET type=?, data=?, lastModified=? WHERE id=?";
    private static final String DELETE
            = "DELETE FROM item WHERE id=?";
    private static final String GET_ONE
            = "SELECT id, type, data, creation, last_modified FROM item WHERE user_id=?";

    private final Connection connection;

    SqlItemCollectionDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(ItemCollection itemCollection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for(ItemPacketData item : itemCollection.getItemCollection()) {
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, item.getType().getName());
                statement.setString(3, item.getEncryptedData());
                statement.setTimestamp(4, Timestamp.valueOf(item.getCreated()));
                statement.setTimestamp(5, Timestamp.valueOf(item.getLastModified()));
            }
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error", e);
        }
    }

    @Override
    public boolean update(ItemCollection itemCollection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            for(ItemPacketData item : itemCollection.getItemCollection()) {
                statement.setString(1, item.getType().getName());
                statement.setString(2, item.getEncryptedData());
                statement.setTimestamp(3, Timestamp.valueOf(item.getLastModified()));
                statement.setString(4, item.getId().toString());
            }
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error", e);
        }
    }

    @Override
    public boolean delete(ItemCollection itemCollection) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            for (ItemPacketData item : itemCollection.getItemCollection()) {
                statement.setString(1, item.getId().toString());
            }
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public Optional<ItemCollection> get(UUID userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(GET_ONE)) {
            statement.setString(1, userId.toString());
            List<ItemPacketData> itemCollection = new ArrayList<>();
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    itemCollection.add(convert(set));
                }
                return Optional.of(new ItemCollection(userId, itemCollection));
            }
        } catch (SQLException e) {
            throw new DaoException("SQL error.", e);
        }
    }

    @Override
    public List<ItemCollection> getAll() throws DaoException {
        return null;
    }

    private static ItemPacketData convert(ResultSet set) throws SQLException {
        UUID id = UUID.fromString(set.getString("id"));
        ItemType type  = ItemType.valueOf(set.getString("type"));
        String data = set.getString("email");
        LocalDateTime created = set.getTimestamp("creation").toLocalDateTime();
        LocalDateTime lastModified = set.getTimestamp("last_modified").toLocalDateTime();
        return new ItemPacketData(id, type, data, created, lastModified);
    }
}
