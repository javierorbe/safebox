package deusto.safebox.client;

import static java.util.stream.Collectors.toSet;

import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.util.Pair;
import deusto.safebox.common.AbstractItem;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ItemParser {

    /**
     * Transforms a collection of {@link AbstractItem} into a collection of {@link ItemData}.
     *
     * @param items the {@link AbstractItem} collection.
     * @return the {@link ItemData} collection.
     */
    public static Collection<ItemData> toItemData(Collection<AbstractItem> items) {
        return items
                .parallelStream()
                .map(ItemData::new)
                .collect(toSet());
    }

    /**
     * Transforms a collection of {@link ItemData}
     * into a list of root folders and a map of {@link LeafItem}s grouped by type.
     *
     * @param itemDataCollection the {@link ItemData} collection.
     * @return a {@link Pair} containing the root folder list and the item map.
     */
    public static Pair<List<Folder>, Map<ItemType, List<LeafItem>>>
    fromItemData(Collection<ItemData> itemDataCollection) {
        throw new UnsupportedOperationException();
    }

    private ItemParser() {
        throw new AssertionError();
    }
}
