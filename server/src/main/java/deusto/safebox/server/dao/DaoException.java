package deusto.safebox.server.dao;

/**
 * Thrown when there is an error during a DAO process.
 *
 * @see Dao
 */
public class DaoException extends Exception {

    private static final long serialVersionUID = 3876717719217497988L;

    public DaoException() {}

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
