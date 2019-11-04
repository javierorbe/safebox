package deusto.safebox.server;

import deusto.safebox.common.ItemData;
import java.util.Collection;
import java.util.UUID;

public class ItemCollection {

    private final UUID userId;
    private final Collection<ItemData> items;

    public ItemCollection(UUID userId, Collection<ItemData> items) {
        this.userId = userId;
        this.items = items;
    }

    public UUID getUserId() {
        return userId;
    }

    public Collection<ItemData> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return userId + "(" + items.size() + " items)";
    }
}
