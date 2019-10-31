package deusto.safebox.server.dao.sql;

import deusto.safebox.server.dao.DaoManager;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public interface SqlDaoManager extends DaoManager, AutoCloseable {

    DatabaseMetaData getDatabaseMetadata() throws SQLException;

    @Override
    void close();
}
