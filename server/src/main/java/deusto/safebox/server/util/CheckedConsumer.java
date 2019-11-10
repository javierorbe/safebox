package deusto.safebox.server.util;

/**
 * A consumer that might throw a {@link Throwable}.
 *
 * @param <T> the type of the consumed object.
 * @param <X> the type of the throwable.
 */
@FunctionalInterface
public interface CheckedConsumer<T, X extends Throwable> {

    void accept(T t) throws X;
}
