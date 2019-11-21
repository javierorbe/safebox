package deusto.safebox.client.datamodel;

import deusto.safebox.client.gui.model.ItemTableModel;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Super class for items contained in a folder.
 * All items (except for folders) should inherit this class.
 */
public abstract class LeafItem extends Item {

    private List<ItemProperty> features= new ArrayList<>();

    /**
     * Creates a {@link LeafItem} and adds itself to the specified folder.
     *
     * @param id the item id.
     * @param type the item type.
     * @param name the item name.
     * @param folder the parent folder.
     * @param created the item creation timestamp.
     * @param lastModified the item last modification timestamp
     */
    LeafItem(UUID id, ItemType type, String name, Folder folder, LocalDateTime created, LocalDateTime lastModified) {
        super(id, type, name, folder, created, lastModified);
        setFeatures(new ArrayList<>(Arrays.asList(
                new ItemProperty<>(name, "Name: "),
                new ItemProperty<>(folder, "Parent's folder: "),
                new ItemProperty<>(created, "Created: "),
                new ItemProperty<>(lastModified, "Last modified: ")
        )));
    }

    /**
     * Returns a property (string, number...) of the item.
     * This is a utility method used in {@link ItemTableModel}.
     *
     * @param index the index of the property.
     * @return the property in the specified index.
     */
    public abstract Object getProperty(int index);

    public List<ItemProperty> getFeatures() {
        return features;
    }

    private void setFeatures(List<ItemProperty> features) {
        this.features = features;
    }
}
