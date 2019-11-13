package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Folder extends Item {

    private final List<LeafItem> items = new ArrayList<>();
    private final List<Folder> subFolders = new ArrayList<>();

    private Folder(UUID id, String name, Folder parentFolder, LocalDateTime created, LocalDateTime lastModified) {
        super(id, ItemType.FOLDER, name, parentFolder, created, lastModified);
    }

    public Folder(UUID id, String name, LocalDateTime created, LocalDateTime lastModified) {
        this(id, name, null, created, lastModified);
    }

    public Folder(String name, LocalDateTime created, LocalDateTime lastModified) {
        this(UUID.randomUUID(), name, created, lastModified);
    }

    @Override
    protected JsonObject getCustomData() {
        return new JsonObject();
    }

    public List<LeafItem> getItems() {
        return items;
    }

    void addItem(LeafItem item) {
        items.add(item);
    }

    public void removeItem(LeafItem item) {
        items.remove(item);
    }

    public List<Folder> getSubFolders() {
        return subFolders;
    }

    public void addSubFolder(Folder folder) {
        subFolders.add(folder);
        folder.setFolder(this);
    }

    private void removeSubFolder(Folder folder) {
        subFolders.remove(folder);
    }

    public boolean isLeafFolder() {
        return subFolders.size() == 0;
    }

    public int getSubFolderCount() {
        return subFolders.size();
    }

    public String getFullPath() {
        StringBuilder builder = new StringBuilder();
        Folder current = this;
        do {
            builder.insert(0, current.getName());
            builder.insert(0, "/");
            current = current.getFolder();
        } while (current != null);
        return builder.toString();
    }

    @Override
    public String toString() {
        return getName();
    }
}
