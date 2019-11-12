package deusto.safebox.server.util;

/**
 * A runnable that might throw a {@link X}.
 *
 * @param <X> the type of the throwable.
 */
@FunctionalInterface
public interface CheckedRunnable<X extends Throwable> {

    void run() throws X;
}
