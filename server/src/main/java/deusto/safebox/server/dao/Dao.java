package deusto.safebox.server.dao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Data access object (DAO).
 * Provides an abstraction to a persistence mechanism.
 *
 * @param <E> the type of the stored object
 * @param <K> the type of the keys of the objects
 * @see <a href="https://en.wikipedia.org/wiki/Data_access_object" target="_top">Data access object in Wikipedia</a>
 */
public interface Dao<E, K> {

    /**
     * Inserts the given object into the storage system asynchronously.
     *
     * <p>The returned {@link CompletableFuture} completes with a {@link Boolean}
     * that is true if and only if the insertion is performed successfully.
     *
     * @param e the object
     * @return a {@code CompletableFuture<Boolean>}
     */
    CompletableFuture<Boolean> insert(E e);

    /**
     * Updates the given object on the storage system asynchronously.
     *
     * <p>The returned {@link CompletableFuture} completes with a {@link Boolean}
     * that is true if and only if the update is performed successfully.
     *
     * @param e the object
     * @return a {@code CompletableFuture<Boolean>}
     */
    CompletableFuture<Boolean> update(E e);

    /**
     * Deletes the given object from the storage system asynchronously.
     *
     * <p>The returned {@link CompletableFuture} completes with a {@link Boolean}
     * that is true if and only if the deletion is performed successfully.
     *
     * @param e the object
     * @return a {@code CompletableFuture<Boolean>}
     */
    CompletableFuture<Boolean> delete(E e);

    /**
     * Returns a {@code CompletableFuture<Optional<E>>} that is completed successfully with
     * an {@link Optional} containing the object associated to the specified key
     * or an empty {@code Optional} if there is no object associated to the key.
     *
     * <p>The returned {@link CompletableFuture} completes exceptionally with {@link DaoException}
     * if an error occurs when getting the object from the storage system.
     *
     * @param key the key whose associated object is to be returned
     * @return an {@code Optional} with the object associated to the specified key
     *         or an empty {@code Optional} if there is no object associated to the key
     */
    CompletableFuture<Optional<E>> get(K key);

    /**
     * Returns a {@code CompletableFuture<List<E>>} that is completed successfully with
     * a list with all the stored objects.
     *
     * <p>The returned {@link CompletableFuture} completes exceptionally with {@link DaoException}
     * if an error occurs when getting the list of objects from the storage system.
     *
     * @return a {@code CompletableFuture<List<E>>}
     */
    CompletableFuture<List<E>> getAll();
}
