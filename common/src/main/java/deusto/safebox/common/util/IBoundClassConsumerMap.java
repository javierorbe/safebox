package deusto.safebox.common.util;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Maps a class type to a {@link Consumer} that consumes an object of that same class type.
 * The class types admitted into the map is limited to subclasses of {@link S}.
 *
 * @param <S> the type of the super class of accepted classes.
 */
public interface IBoundClassConsumerMap<S> {

    /**
     * Associates the specified class with the specified {@link Consumer}.
     *
     * @param classType the class.
     * @param consumer {@link Consumer} to be associated with the specified class.
     * @param <T> the type of the key class.
     */
    <T extends S> void put(Class<T> classType, Consumer<T> consumer);

    /**
     * Returns an {@link Optional} containing the consumer to which the specified class
     * is mapped or an empty {@link Optional} if this map contains no mapping for the class.
     *
     * @param classType the class whose associated {@link Consumer} is to be returned.
     * @param <T> the type of the key class.
     * @return the consumer to which the specified class is mapped
     *          or an empty {@link Optional} if this map contains no mapping for the class.
     */
    <T extends S> Optional<Consumer<T>> get(Class<T> classType);

    /**
     * Returns an {@link Optional} containing the consumer to which the specified class
     * is mapped or an empty {@link Optional} if this map contains no mapping for the class.
     *
     * @param object an object of the required class.
     * @param <T> the type of the key class.
     * @return the consumer to which the specified class is mapped
     *          or an empty {@link Optional} if this map contains no mapping for the class.
     */
    <T extends S> Optional<Consumer<T>> of(T object);
}
