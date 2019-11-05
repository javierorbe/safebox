package deusto.safebox.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * An unbound implementation of {@link IBoundClassConsumerMap}.
 */
public class ClassConsumerMap implements IBoundClassConsumerMap<Object> {

    // Both wildcards must be of the same type (Java's type system is not powerful enough to express this).
    private final Map<Class<?>, Consumer<?>> consumers = new HashMap<>();

    @Override
    public <T> void put(Class<T> classType, Consumer<T> consumer) {
        consumers.put(Objects.requireNonNull(classType), consumer);
    }

    @Override
    public <T> Optional<Consumer<T>> get(T object) {
        // Type safe because there is a type relationship between keys and values.
        @SuppressWarnings("unchecked")
        Consumer<T> consumer = (Consumer<T>) consumers.get(object.getClass());
        return Optional.ofNullable(consumer);
    }
}
