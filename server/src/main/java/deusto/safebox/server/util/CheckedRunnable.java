package deusto.safebox.server.util;

/**
 * A runnable that can throw a checked exception.
 *
 * @param <X> the type of the exception
 */
@FunctionalInterface
public interface CheckedRunnable<X extends Exception> {

    void run() throws X;
}
