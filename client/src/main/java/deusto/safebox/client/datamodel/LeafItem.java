package deusto.safebox.client.datamodel;

import java.time.LocalDateTime;

public abstract class LeafItem extends Item {

    private Folder folder;

    LeafItem(String name, Folder folder, LocalDateTime created, LocalDateTime lastModified) {
        super(name, created, lastModified);
        this.folder = folder;
    }

    public Folder getFolder() {
        return folder;
    }
}
