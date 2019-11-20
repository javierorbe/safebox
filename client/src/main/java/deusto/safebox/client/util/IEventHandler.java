package deusto.safebox.client.util;

import java.util.function.Consumer;

/**
 * Event handler with listeners that consume an object of type {@link T}.
 *
 * @param <T> the type of the consumed object.
 */
public interface IEventHandler<T> {

    /**
     * Adds a listener to this event.
     *
     * @param consumer the listener operation.
     */
    void addListener(Consumer<T> consumer);

    /**
     * Fires the event with the specified parameter.
     *
     * @param object the object consumed by the event listeners.
     */
    void fire(T object);
}
