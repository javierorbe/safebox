package deusto.safebox.client.datamodel;

import deusto.safebox.client.datamodel.property.ItemProperty;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Super class for items contained in a folder.
 * All items (except for folders) should inherit this class.
 */
public abstract class LeafItem extends Item {

    private final List<ItemProperty<?>> properties = new ArrayList<>();

    /**
     * Creates a {@link LeafItem}.
     *
     * @param id the item id.
     * @param type the item type.
     * @param title the title of the item.
     * @param folder the parent folder.
     * @param created the item creation timestamp.
     * @param lastModified the item last modification timestamp
     */
    LeafItem(UUID id, ItemType type, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified) {
        super(id, type, title, folder, created, lastModified);
        properties.add(this.title);
    }

    public List<ItemProperty<?>> getProperties() {
        return properties;
    }

    /**
     * Returns a the property of the item at the specified index.
     *
     * @param index the index of the property.
     * @return the property at the specified index.
     */
    public ItemProperty<?> getProperty(int index) {
        return properties.get(index);
    }

    void addProperties(List<ItemProperty<?>> properties) {
        this.properties.addAll(properties);
        this.properties.add(this.created);
        this.properties.add(this.lastModified);
    }
}
