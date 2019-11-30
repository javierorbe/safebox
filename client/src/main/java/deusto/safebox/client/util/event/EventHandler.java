package deusto.safebox.client.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles events of type {@link E}.
 *
 * <p>The event listeners are {@link Consumer}s that take a subtype of the base event.
 *
 * @param <E> the supertype of the handled events, the base event type.
 */
public class EventHandler<E> {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    // Both wildcards must be of the same type.
    private final Map<Class<? extends E>, Collection<Consumer<? extends E>>> listenerMap = new HashMap<>();

    /**
     * Registers the methods of a {@link Listener} marked with {@link EventListener}
     * as listeners of the event type of their parameter.
     *
     * @param listener the listener that contains the methods to register.
     */
    public void registerListeners(Listener listener) {
        Method[] publicMethods = getClass().getMethods();
        Method[] privateMethods = getClass().getDeclaredMethods();
        Collection<Method> methods = new HashSet<>();
        methods.addAll(Arrays.asList(publicMethods));
        methods.addAll(Arrays.asList(privateMethods));

        for (Method method : methods) {
            if (!method.isAnnotationPresent(EventListener.class)) {
                continue;
            }

            ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
            @SuppressWarnings("unchecked")
            Class<E> paramTypeClass = (Class<E>) superclass.getActualTypeArguments()[0];
            // The class of the method parameter.
            final Class<? extends E> methodParamClass;
            // The method must have one parameter and it must be a subtype of
            // the parameter type of the instance of this class.
            if (method.getParameterTypes().length == 1
                    || !paramTypeClass.isAssignableFrom(method.getParameterTypes()[0])) {
                @SuppressWarnings("unchecked")
                Class<? extends E> actualClass = (Class<? extends E>) method.getParameterTypes()[0];
                methodParamClass = actualClass;
            } else {
                logger.error("Attempted to register an invalid EventListener method signature.");
                continue;
            }

            method.setAccessible(true);
            Collection<Consumer<? extends E>> collection
                    = listenerMap.computeIfAbsent(methodParamClass, c -> new HashSet<>());
            Consumer<? extends E> consumer = object -> {
                try {
                    method.invoke(listener, object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            };
            collection.add(consumer);
        }
    }

    /**
     * Registers a listener for the specified event type.
     *
     * @param eventClass the event type class.
     * @param consumer the listener action.
     * @param <X> the event type.
     */
    public <X extends E> void registerListener(Class<X> eventClass, Consumer<X> consumer) {
        Collection<Consumer<? extends E>> collection
                = listenerMap.computeIfAbsent(eventClass, c -> new HashSet<>());
        collection.add(consumer);
    }

    /**
     * Fires an event type with the specified event object.
     *
     * @param object the event object.
     * @param <X> the event type.
     */
    public <X extends E> void fire(X object) {
        Optional.ofNullable(listenerMap.get(object.getClass()))
                .ifPresentOrElse(listeners -> listeners.forEach(consumer -> {
                    @SuppressWarnings("unchecked")
                    Consumer<X> castedConsumer = (Consumer<X>) consumer;
                    castedConsumer.accept(object);
                }), () -> logger.error(
                        "There is no action defined for the received object ({}).",
                        object.getClass().getName())
                );
    }
}
