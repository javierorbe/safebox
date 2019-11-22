package deusto.safebox.client.net;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.ItemParser;
import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.util.EventHandler;
import deusto.safebox.client.util.IEventHandler;
import deusto.safebox.client.util.Pair;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.net.packet.ErrorPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.RetrieveDataPacket;
import deusto.safebox.common.net.packet.SuccessfulRegisterPacket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/** Handles the received packets. */
public class PacketHandler {

    private static final Map<Class<? extends Packet>, IEventHandler<? extends Packet>> EVENT_HANDLER_MAP
            = new HashMap<>();

    static {
        addListener(ErrorPacket.class, PacketHandler::onError);
        addListener(RetrieveDataPacket.class, PacketHandler::onReceiveData);
        addListener(SuccessfulRegisterPacket.class, PacketHandler::onSuccessfulRegister);
        // TODO: add the remaining listeners
    }

    /**
     * Registers a listener of a given packet type.
     *
     * @param classType the class of the packet.
     * @param consumer the event callback.
     * @param <T> the class type of the packet.
     */
    public static <T extends Packet> void addListener(Class<T> classType, Consumer<T> consumer) {
        // Type safe.
        @SuppressWarnings("unchecked")
        EventHandler<T> eventHandler
                = (EventHandler<T>) EVENT_HANDLER_MAP.computeIfAbsent(classType, type -> new EventHandler<T>());
        eventHandler.addListener(consumer);
    }

    /**
     * Calls the listeners of a given packet type with the provided packet.
     *
     * @param object the packet.
     * @param <T> the packet type.
     */
    public static <T extends Packet> void fire(T object) {
        // Type safe because the key (the class type) always matches the event handler type.
        @SuppressWarnings("unchecked")
        EventHandler<T> eventHandler = (EventHandler<T>) EVENT_HANDLER_MAP.get(object.getClass());
        eventHandler.fire(object);
    }

    private static void onError(ErrorPacket packet) {
        ErrorHandler.fire(packet.getErrorType());
    }

    private static void onReceiveData(RetrieveDataPacket packet) {
        Pair<List<Folder>, Map<ItemType, List<LeafItem>>> decryptPair = ItemParser.fromItemData(packet.getItems());
        ItemManager.set(decryptPair.getLeft(), decryptPair.getRight());
    }

    private static void onSuccessfulRegister(SuccessfulRegisterPacket packet) {
        // TODO: show successful register dialog
    }
}
