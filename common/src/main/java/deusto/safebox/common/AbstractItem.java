package deusto.safebox.common;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractItem {

    private UUID id;
    private final LocalDateTime created;
    private LocalDateTime lastModified;

    public AbstractItem(UUID id, LocalDateTime created, LocalDateTime lastModified) {
        this.id = id;
        this.created = created;
        this.lastModified = lastModified;
    }

    public AbstractItem(LocalDateTime created, LocalDateTime lastModified) {
        this(null, created, lastModified);
    }

    public UUID getItemId() {
        return id;
    }

    public void setItemId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public abstract ItemType getItemType();

    /**
     * Returns an encrypted JSON string with the data specific to an item type.
     *
     * @return an encrypted string of data.
     */
    public abstract String getEncryptedData();
}
