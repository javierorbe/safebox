package deusto.safebox.server.dao.sql;

import deusto.safebox.server.util.CheckedRunnable;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SqlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlUtil.class);

    /**
     * Performs a transaction.
     *
     * @param connection the connection where the transaction is performed
     * @param operation the transaction operation
     * @return {@code true} if the transaction succeeds, otherwise false
     */
    static boolean transaction(Connection connection, CheckedRunnable<SQLException> operation) {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.error("Error disabling auto commit.", e);
            return false;
        }
        try {
            operation.run();
            connection.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.error("Error in transaction.", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.error("Error in transaction rollback.", rollbackEx);
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Error enabling auto commit.");
            }
        }
    }

    private SqlUtil() {
        throw new AssertionError();
    }
}
