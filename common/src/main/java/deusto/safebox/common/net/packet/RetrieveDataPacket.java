package deusto.safebox.common.net.packet;

import deusto.safebox.common.ItemData;
import java.io.Serializable;
import java.util.Collection;

public class RetrieveDataPacket extends Packet implements Serializable {

    private static final long serialVersionUID = -4010470347016376157L;

    private final Collection<ItemData> items;

    public RetrieveDataPacket(Collection<ItemData> items) {
        this.items = items;
    }

    public Collection<ItemData> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + getItems().size() + " items)";
    }
}
