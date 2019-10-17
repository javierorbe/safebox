package deusto.safebox.server.dao;

import java.util.List;
import java.util.Optional;

/**
 * Data access object.
 *
 * @param <E> object type.
 * @param <K> key data type.
 * @see <a href="https://en.wikipedia.org/wiki/Data_access_object" target="_top">Data access object in Wikipedia</a>
 */
public interface Dao<E, K> {

    boolean insert(E e) throws DaoException;

    boolean update(E e) throws DaoException;

    boolean delete(E e) throws DaoException;

    Optional<E> get(K key) throws DaoException;

    List<E> getAll() throws DaoException;
}
