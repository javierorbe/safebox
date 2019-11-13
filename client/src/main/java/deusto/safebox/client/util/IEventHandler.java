package deusto.safebox.client.util;

import java.util.function.Consumer;

/**
 * Event handler of with listeners that consume an object.
 *
 * @param <T> the type of the consumed object.
 */
public interface IEventHandler<T> {

    void addListener(Consumer<T> consumer);

    void fire(T object);
}
