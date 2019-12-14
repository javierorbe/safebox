package deusto.safebox.client.datamodel;

import deusto.safebox.client.datamodel.property.ItemProperty;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an {@link Item} contained in a {@link Folder}.
 *
 * <p>All items (except folders) should inherit this class.
 */
public abstract class LeafItem extends Item {

    private final List<ItemProperty<?>> properties = new ArrayList<>();

    /**
     * Constructs a {@code LeafItem} with the specified properties.
     *
     * @param id the item id
     * @param type the item type
     * @param title the title of the item
     * @param folder the parent folder
     * @param created the item creation timestamp
     * @param lastModified the item last modification timestamp
     */
    LeafItem(UUID id, ItemType type, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified) {
        super(id, type, title, folder, created, lastModified);
    }

    public List<ItemProperty<?>> getProperties() {
        return properties;
    }

    /**
     * Returns the property of this item at the specified index.
     *
     * @param index the index of the property
     * @return the property at the specified index
     */
    public ItemProperty<?> getProperty(int index) {
        return properties.get(index);
    }

    /**
     * Builds the {@link #properties} list.
     */
    void addProperties(List<ItemProperty<?>> properties) {
        this.properties.add(title);
        this.properties.addAll(properties);
        this.properties.add(created);
        this.properties.add(lastModified);
    }
}
