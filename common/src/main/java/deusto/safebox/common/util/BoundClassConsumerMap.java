package deusto.safebox.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * An implementation of {@link IBoundClassConsumerMap}.
 *
 * @param <S> the super class of accepted class types.
 */
public class BoundClassConsumerMap<S> implements IBoundClassConsumerMap<S> {

    // Both wildcards must be of the same type (Java's type system is not powerful enough to express this).
    private final Map<Class<? extends S>, Consumer<? extends S>> consumers = new HashMap<>();

    @Override
    public <T extends S> void put(Class<T> classType, Consumer<T> consumer) {
        consumers.put(Objects.requireNonNull(classType), consumer);
    }

    @Override
    public <T extends S> Optional<Consumer<T>> get(Class<T> classType) {
        // Type safe because there is a type relationship between keys and values.
        @SuppressWarnings("unchecked")
        Consumer<T> consumer = (Consumer<T>) consumers.get(classType);
        return Optional.ofNullable(consumer);
    }

    @Override
    public <T extends S> Optional<Consumer<T>> of(T object) {
        // Type safe because there is a type relationship between keys and values.
        @SuppressWarnings("unchecked")
        Consumer<T> consumer = (Consumer<T>) consumers.get(object.getClass());
        return Optional.ofNullable(consumer);
    }
}
