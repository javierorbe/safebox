package deusto.safebox.client.net;

import deusto.safebox.common.net.packet.ErrorPacket;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * Event handler for specified error types.
 * Singleton.
 */
public enum ErrorHandler {
    INSTANCE;

    private final Map<ErrorPacket.ErrorType, Collection<Runnable>> listeners
            = new EnumMap<>(ErrorPacket.ErrorType.class);

    public void addListener(ErrorPacket.ErrorType type, Runnable action) {
        listeners.computeIfAbsent(type, e -> new HashSet<>()).add(action);
    }

    void fire(ErrorPacket.ErrorType type) {
        Optional.ofNullable(listeners.get(type))
                .ifPresent(listeners -> listeners.forEach(Runnable::run));
    }
}
