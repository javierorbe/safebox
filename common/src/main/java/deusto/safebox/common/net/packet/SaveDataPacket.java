package deusto.safebox.common.net.packet;

import deusto.safebox.common.EncryptedItem;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SaveDataPacket extends Packet {

    private Set<EncryptedItem> items = new HashSet<>();

    public void addItem(EncryptedItem item) {
        items.add(item);
    }

    public Collection<EncryptedItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "SaveItemsPacket: " + Arrays.toString(items.toArray());
    }
}
