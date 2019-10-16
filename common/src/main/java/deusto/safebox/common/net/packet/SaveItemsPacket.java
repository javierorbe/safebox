package deusto.safebox.common.net.packet;

import deusto.safebox.common.ItemContainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SaveItemsPacket extends Packet {

    private Set<ItemContainer> items = new HashSet<>();

    public void addItem(ItemContainer item) {
        items.add(item);
    }

    public Collection<ItemContainer> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "SaveItemsPacket: " + Arrays.toString(items.toArray());
    }
}
