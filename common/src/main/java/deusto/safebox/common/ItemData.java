package deusto.safebox.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/** Serializable form of an item. */
public class ItemData implements Serializable {

    private static final long serialVersionUID = 4554854569406609630L;

    private final UUID id;
    private final ItemType type;
    private final String data;
    private final LocalDateTime created;
    private final LocalDateTime lastModified;

    public ItemData(UUID id, ItemType type, String data, LocalDateTime created, LocalDateTime lastModified) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.created = created;
        this.lastModified = lastModified;
    }

    public ItemData(AbstractItem item) {
        this(item.getItemId(), item.getItemType(), item.getEncryptedData(), item.getCreated(), item.getLastModified());
    }

    public UUID getId() {
        return id;
    }

    public ItemType getType() {
        return type;
    }

    public String getEncryptedData() {
        return data;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }
}
