package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Folder extends Item {

    private Folder parentFolder;
    private final List<LeafItem> items = new ArrayList<>();
    private final List<Folder> subFolders = new ArrayList<>();

    public Folder(String name, Folder parentFolder, LocalDateTime created, LocalDateTime lastModified) {
        super(name, created, lastModified);
        this.parentFolder = parentFolder;
    }

    public Folder(String name, LocalDateTime created, LocalDateTime lastModified) {
        this(name, null, created, lastModified);
    }

    @Override
    public ItemType getItemType() {
        return ItemType.FOLDER;
    }

    @Override
    protected JsonObject getCustomData() {
        return new JsonObject();
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public List<LeafItem> getItems() {
        return items;
    }

    public void addItem(LeafItem item) {
        items.add(item);
        item.setFolder(this);
    }

    public List<Folder> getSubFolders() {
        return subFolders;
    }

    public void addSubFolder(Folder folder) {
        subFolders.add(folder);
        folder.parentFolder = this;
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
            builder.append('/');
            current = current.getParentFolder();
        } while (current != null);
        builder.append('/');
        return builder.toString();
    }

    @Override
    public String toString() {
        return getName();
    }
}
