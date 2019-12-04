package deusto.safebox.client.net;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.ItemParser;
import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.util.Pair;
import deusto.safebox.client.util.event.EventHandler;
import deusto.safebox.client.util.event.EventListener;
import deusto.safebox.client.util.event.Listener;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.net.packet.ErrorPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.RetrieveDataPacket;
import deusto.safebox.common.net.packet.SuccessfulRegisterPacket;
import java.util.List;
import java.util.Map;

/**
 * Handles the received packets.
 * Singleton.
 */
public class PacketHandler extends EventHandler<Packet> implements Listener {

    public static final PacketHandler INSTANCE = new PacketHandler();

    private PacketHandler() {
        registerListeners(this);
    }

    @EventListener
    private static void onError(ErrorPacket packet) {
        ErrorHandler.fire(packet.getErrorType());
    }

    @EventListener
    private static void onRetrieveData(RetrieveDataPacket packet) {
        Pair<List<Folder>, Map<ItemType, List<LeafItem>>> decryptPair = ItemParser.fromItemData(packet.getItems());
        ItemManager.set(decryptPair.getLeft(), decryptPair.getRight());
    }
}
