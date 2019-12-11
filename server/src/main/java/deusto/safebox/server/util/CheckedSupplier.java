package deusto.safebox.server.util;

/**
 * A supplier that can throw a checked exception.
 *
 * @param <T> the type of the supplied object
 * @param <X> the type of the exception
 */
@FunctionalInterface
public interface CheckedSupplier<T, X extends Exception> {

    T get() throws X;
}
