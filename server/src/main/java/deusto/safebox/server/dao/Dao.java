package deusto.safebox.server.dao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
     */
    CompletableFuture<Boolean> insert(E e);

    /**
     * Updates an object on the storage system.
     *
     * @param e the object.
     * @return true if the update is successful, otherwise false.
     */
    CompletableFuture<Boolean> update(E e);

    /**
     * Deletes an object from the storage system.
     *
     * @param e the object.
     * @return true if the deletion is successful, otherwise false.
     */
    CompletableFuture<Boolean> delete(E e);

    /**
     * Returns an {@link Optional} with the object associated to the specified key
     * or an empty {@code Optional} if there is no object associated to the key.
     *
     * @param key the key whose associated object is to be returned.
     * @return an {@code Optional} with the object associated to the specified key
     *         or an empty {@code Optional} if there is no object associated to the key.
     */
    CompletableFuture<Optional<E>> get(K key);

    /**
     * Returns a list with all the stored objects.
     *
     * @return a list with all the stored objects.
     */
    CompletableFuture<List<E>> getAll();
}
