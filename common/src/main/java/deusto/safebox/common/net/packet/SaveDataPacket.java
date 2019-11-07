package deusto.safebox.common.net.packet;

import deusto.safebox.common.ItemData;
import java.io.Serializable;
import java.util.Collection;

public class SaveDataPacket extends Packet implements Serializable {

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
        return getClass().getName() + " (" + items.size() + " items)";
    }
}
