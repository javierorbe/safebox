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

    private final Map<Class<? extends S>, Consumer<? extends S>> consumers = new HashMap<>();

    @Override
    public <T extends S> void put(Class<T> classType, Consumer<T> consumer) {
        consumers.put(Objects.requireNonNull(classType), consumer);
    }

    @Override
    public <T extends S> Optional<Consumer<T>> get(T object) {
        @SuppressWarnings("unchecked")
        Consumer<T> consumer = (Consumer<T>) consumers.get(object.getClass());
        return Optional.ofNullable(consumer);
    }
}
