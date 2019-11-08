package deusto.safebox.common;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractItem {

    private final UUID id;
    private final ItemType type;
    private final LocalDateTime created;
    private LocalDateTime lastModified;

    public AbstractItem(UUID id, ItemType type, LocalDateTime created, LocalDateTime lastModified) {
        this.id = id;
        this.type = type;
        this.created = created;
        this.lastModified = lastModified;
    }

    public UUID getId() {
        return id;
    }

    public ItemType getType() {
        return type;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Returns an encrypted JSON string with the data specific to the type of the item.
     *
     * @return an encrypted string of data.
     */
    public abstract String getEncryptedData();
}
