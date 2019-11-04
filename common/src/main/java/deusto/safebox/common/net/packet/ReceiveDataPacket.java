package deusto.safebox.common.net.packet;

import deusto.safebox.common.ItemData;
import java.util.Collection;

public class ReceiveDataPacket extends Packet {

    private static final long serialVersionUID = -4010470347016376157L;

    private final Collection<ItemData> items;

    public ReceiveDataPacket(Collection<ItemData> items) {
        this.items = items;
    }

    public Collection<ItemData> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "ReceiveDataPacket";
    }
}
