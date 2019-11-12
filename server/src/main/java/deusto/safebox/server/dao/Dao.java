package deusto.safebox.server.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Data access object (DAO).
 * Provides an abstraction to a persistence mechanism.
 *
 * @param <E> the type of the stored object.
 * @param <K> the type of the keys of the objects.
 * @see <a href="https://en.wikipedia.org/wiki/Data_access_object" target="_top">Data access object in Wikipedia</a>
 */
public interface Dao<E, K> {

    /**
     * Inserts an object into the storage system.
     *
     * @param e the object.
     * @return true if the insertion is successful, otherwise false.
     * @throws DaoException if there is an error on the insertion.
     */
    boolean insert(E e) throws DaoException;

    /**
     * Updates an object on the storage system.
     *
     * @param e the object.
     * @return true if the update is successful, otherwise false.
     * @throws DaoException if there is an error on the update.
     */
    boolean update(E e) throws DaoException;

    /**
     * Deletes an object from the storage system.
     *
     * @param e the object.
     * @return true if the deletion is successful, otherwise false.
     * @throws DaoException if there is an error on the deletion.
     */
    boolean delete(E e) throws DaoException;

    /**
     * Returns an {@link Optional} with the object associated to the specified key
     * or an empty {@link Optional} if there is no object associated to the key.
     *
     * @param key the key whose associated object is to be returned.
     * @return an {@link Optional} with the object associated to the specified key
     *         or an empty {@link Optional} if there is no object associated to the key.
     * @throws DaoException if there is an error getting the object.
     */
    Optional<E> get(K key) throws DaoException;

    /**
     * Returns a list with all the stored objects.
     *
     * @return a list with all the stored objects.
     * @throws DaoException if there is an error getting the list.
     */
    List<E> getAll() throws DaoException;
}
