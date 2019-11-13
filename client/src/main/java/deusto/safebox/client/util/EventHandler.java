package deusto.safebox.client.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * {@link IEventHandler} implementation.
 */
public class EventHandler<T> implements IEventHandler<T> {

    private final Collection<Consumer<T>> listeners = new HashSet<>();

    @Override
    public void addListener(Consumer<T> consumer) {
        listeners.add(consumer);
    }

    @Override
    public void fire(T object) {
        listeners.forEach(listener -> listener.accept(object));
    }
}
