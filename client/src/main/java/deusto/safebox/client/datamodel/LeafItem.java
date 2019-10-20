package deusto.safebox.client.datamodel;

import deusto.safebox.client.gui.model.ItemTableModel;
import java.time.LocalDateTime;

/**
 * Super type for items contained in a folder.
 * All items (except for folders) should inherit this class.
 */
public abstract class LeafItem extends Item {

    private Folder folder;

    LeafItem(String name, Folder folder, LocalDateTime created, LocalDateTime lastModified) {
        super(name, created, lastModified);
        this.folder = folder;
    }

    LeafItem(String name, LocalDateTime created, LocalDateTime lastModified) {
        this(name, null, created, lastModified);
    }

    /**
     * Returns the folder where the item is.
     *
     * @return a reference of the folder.
     */
    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    /**
     * Returns a property (string, number...) of the item.
     * This is a utility method used in {@link ItemTableModel}.
     *
     * @param index the index of the property.
     * @return the property in the specified index.
     */
    public abstract Object getProperty(int index);
}
