package deusto.safebox.server;

import deusto.safebox.common.net.ItemPacketData;
import java.util.Collection;
import java.util.UUID;

public class ItemCollection {

    private final UUID userId;
    private final Collection<ItemPacketData> items;

    public ItemCollection(UUID userId, Collection<ItemPacketData> items) {
        this.userId = userId;
        this.items = items;
    }

    public UUID getUserId() {
        return userId;
    }

    public Collection<ItemPacketData> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return userId + "(" + items.size() + " items)";
    }
}
