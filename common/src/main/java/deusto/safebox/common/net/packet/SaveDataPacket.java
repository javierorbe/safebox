package deusto.safebox.common.net.packet;

import deusto.safebox.common.ItemData;
import java.util.Collection;

/** Packet sent by a client to store their items on the app server. */
public class SaveDataPacket extends Packet {

    private static final long serialVersionUID = 6465366035399352082L;

    private final Collection<ItemData> items;

    public SaveDataPacket(Collection<ItemData> items) {
        this.items = items;
    }

    public Collection<ItemData> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + items.size() + " items)";
    }
}
