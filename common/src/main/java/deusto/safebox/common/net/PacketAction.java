package deusto.safebox.common.net;

import deusto.safebox.common.net.packet.Packet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class PacketAction {

    // Both wildcards must be of the same type (Java's type system is not powerful enough to express this).
    private final Map<Class<? extends Packet>, Consumer<? extends Packet>> actions = new HashMap<>();

    public <P extends Packet> void putAction(Class<P> type, Consumer<P> consumer) {
        actions.put(Objects.requireNonNull(type), consumer);
    }

    public <P extends Packet> Optional<Consumer<P>> getAction(P packet) {
        // Type safe because there is a type relationship between keys and values.
        @SuppressWarnings("unchecked")
        Consumer<P> consumer = (Consumer<P>) actions.get(packet.getClass());
        return Optional.ofNullable(consumer);
    }
}
