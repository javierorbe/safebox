package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.security.ClientSecurity;
import deusto.safebox.common.AbstractItem;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.UUID;

abstract class Item extends AbstractItem {

    private String name;
    private Folder folder;

    /**
     * Creates an abstract item representation.
     *
     * @param id the item id.
     * @param type the item type.
     * @param name the name of the item.
     * @param folder item's parent folder.
     * @param created the item creation timestamp.
     * @param lastModified the timestamp when item was last modified.
     */
    Item(UUID id, ItemType type, String name, Folder folder, LocalDateTime created, LocalDateTime lastModified) {
        super(id, type, created, lastModified);
        this.name = name;
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    /** Returns the parent folder of this item. */
    public Folder getFolder() {
        return folder;
    }

    /** Sets the parent folder of this item. */
    public void setFolder(Folder folder) {
        this.folder = folder;
    }



    /**
     * Returns a {@link JsonObject} containing the properties that are specific to the item type.
     *
     * @return a {@link JsonObject} with the data.
     */
    abstract JsonObject getCustomData();

    @Override
    protected String getEncryptedData() {
        JsonObject root = getCustomData();
        root.addProperty("name", name);
        String folderId = folder == null ? Folder.NO_PARENT_FOLDER_ID : folder.getId().toString();
        root.addProperty("folder", folderId);
        return ClientSecurity.encrypt(root.toString());
    }
}
