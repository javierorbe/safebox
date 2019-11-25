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

public class EventHandler<E> {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    private final Map<Class<?>, Collection<Consumer<?>>> listenerMap = new HashMap<>();

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

            ParameterizedType param = (ParameterizedType) getClass().getGenericSuperclass();
            @SuppressWarnings("unchecked")
            Class<E> paramTypeClass = (Class<E>) param.getActualTypeArguments()[0];
            Class<?> checkClass;
            if (method.getParameterTypes().length != 1
                    || !paramTypeClass.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                logger.error("Attempted to register an invalid EventListener method signature.");
                continue;
            }

            method.setAccessible(true);
            Collection<Consumer<?>> collection = listenerMap.computeIfAbsent(checkClass, c -> new HashSet<>());
            Consumer<?> consumer = object -> {
                try {
                    method.invoke(listener, object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            };
            collection.add(consumer);
        }
    }

    public <X extends E> void addListener(Class<X> eventClass, Consumer<X> consumer) {
        Collection<Consumer<?>> collection = listenerMap.computeIfAbsent(eventClass, c -> new HashSet<>());
        collection.add(consumer);
    }

    // This should work, but there is a bug in the compiler
    // that causes lambdas to behave different to anonymous class instances.
    // Someone with a similar problem:
    // https://stackoverflow.com/questions/53039980/different-generic-behaviour-when-using-lambda-instead-of-explicit-anonymous-inne
    private <X extends E> void addListener(Consumer<X> consumer) {
        ParameterizedType paramType = (ParameterizedType) consumer.getClass().getGenericInterfaces()[0];
        @SuppressWarnings("unchecked")
        Class<X> typeClass = (Class<X>) paramType.getActualTypeArguments()[0];
        Collection<Consumer<?>> collection = listenerMap.computeIfAbsent(typeClass, c -> new HashSet<>());
        collection.add(consumer);
    }

    public <X extends E> void fire(X object) {
        Optional.ofNullable(listenerMap.get(object.getClass()))
                .ifPresentOrElse(listeners -> {
                    listeners.forEach(consumer -> {
                        @SuppressWarnings("unchecked")
                        Consumer<X> castedConsumer = (Consumer<X>) consumer;
                        castedConsumer.accept(object);
                    });
                }, () -> logger.error(
                        "There is no action defined for the received object ({}).",
                        object.getClass().getName()));
    }
}
