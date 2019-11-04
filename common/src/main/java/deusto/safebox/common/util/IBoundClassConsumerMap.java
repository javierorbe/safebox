package deusto.safebox.common.util;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Maps a class type to a {@link Consumer} that consumes an object of that same class type.
 * The class types admitted into the map is limited to subclasses of {@link S}.
 */
public interface IBoundClassConsumerMap<S> {

    <T extends S> void put(Class<T> classType, Consumer<T> consumer);

    <T extends S> Optional<Consumer<T>> get(T object);
}
