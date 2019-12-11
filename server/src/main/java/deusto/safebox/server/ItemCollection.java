package deusto.safebox.server;

import deusto.safebox.common.ItemData;
import java.util.Collection;
import java.util.UUID;

/** Immutable representation of a collection of items owned by a user. */
public class ItemCollection {

    private final UUID userId;
    private final Collection<ItemData> items;

    /**
     * Constructs an {@code ItemCollection} for the specified user and items.
     *
     * @param userId the ID of the user that owns the item collection
     * @param items the items
     */
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
        return userId + " (" + items.size() + " items)";
    }
}
