package deusto.safebox.server.util;

/**
 * A supplier that might throw a {@link X}.
 *
 * @param <T> the type of the supplied object.
 * @param <X> the type of the throwable.
 */
@FunctionalInterface
public interface CheckedSupplier<T, X extends Throwable> {

    T get() throws X;
}
