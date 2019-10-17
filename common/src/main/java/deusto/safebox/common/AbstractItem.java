package deusto.safebox.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractItem implements Serializable {

    private UUID id;

    private LocalDateTime created;
    private LocalDateTime lastModified;

    public AbstractItem(LocalDateTime created, LocalDateTime lastModified) {
        this.created = created;
        this.lastModified = lastModified;
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
