package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Folder extends Item {

    public static final String NO_PARENT_FOLDER_ID = "none";

    private final List<LeafItem> items = new ArrayList<>();
    private final List<Folder> subFolders = new ArrayList<>();

    public Folder(UUID id, String name, LocalDateTime created, LocalDateTime lastModified) {
        super(id, ItemType.FOLDER, name, null, created, lastModified);
    }

    public Folder(String name) {
        this(UUID.randomUUID(), name, LocalDateTime.now(), LocalDateTime.now());
    }

    @Override
    JsonObject getCustomData() {
        return new JsonObject();
    }

    public List<LeafItem> getItems() {
        return items;
    }

    public void addItem(LeafItem item) {
        items.add(item);
    }

    public void removeItem(LeafItem item) {
        items.remove(item);
    }

    public List<Folder> getSubFolders() {
        return subFolders;
    }

    public List<Folder> getAllSubFolders() {
        List<Folder> folders = new ArrayList<>(getSubFolders());
        getSubFolders().forEach(subFolder -> {
            if (!subFolder.isLeafFolder()) {
                folders.addAll(subFolder.getSubFolders());
            }
        });
        return folders;
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

    private boolean isRootFolder() {
        return getFolder() == null;
    }

    public int getSubFolderCount() {
        return subFolders.size();
    }

    /** Returns the full path of the folder. */
    public String getFullPath() {
        StringBuilder builder = new StringBuilder();
        getFullPath(builder);
        return builder.toString();
    }

    /** Build the folder path recursively. */
    private void getFullPath(StringBuilder builder) {
        // Put the current folder name at the start of the string.
        builder.insert(0, '/' + getTitle());
        if (!isRootFolder()) {
            getFolder().getFullPath(builder);
        }
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
