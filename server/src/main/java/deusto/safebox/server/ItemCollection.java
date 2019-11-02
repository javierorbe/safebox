package deusto.safebox.server;

import deusto.safebox.common.net.ItemPacketData;
import java.util.Collection;
import java.util.UUID;

public class ItemCollection {

    private UUID userId;
    private Collection<ItemPacketData> itemCollection;

    public ItemCollection(UUID userId, Collection<ItemPacketData> itemCollection){
        this.userId = userId;
        this.itemCollection = itemCollection;
    }

    public UUID getUserId() {
        return userId;
    }

    public Collection<ItemPacketData> getItemCollection() {
        return itemCollection;
    }
}
