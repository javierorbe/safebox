package deusto.safebox.common.net;

import deusto.safebox.common.net.packet.Packet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class PacketAction {

    // Both wildcards must be of the same type (Java's type system is not powerful enough to express this).
    private final Map<Class<? extends Packet>, Consumer<? extends Packet>> actions = new HashMap<>();

    public <P extends Packet> void putAction(Class<P> type, Consumer<P> consumer) {
        actions.put(Objects.requireNonNull(type), consumer);
    }

    // Type safe because there is a type relationship between keys and values.
    @SuppressWarnings("unchecked")
    public <P extends Packet> Consumer<P> getAction(P packet) {
        return (Consumer<P>) actions.get(packet.getClass());
    }
}
