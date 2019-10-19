package deusto.safebox.common.net;

import deusto.safebox.common.AbstractItem;
import deusto.safebox.common.ItemType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/** Serializable form of an item. */
public class ItemPacketData implements Serializable {

    private static final long serialVersionUID = 4554854569406609630L;

    private final UUID id;
    private final ItemType type;
    private final String data;
    private final LocalDateTime created;
    private final LocalDateTime lastModified;

    public ItemPacketData(AbstractItem item) {
        id = item.getItemId();
        type = item.getItemType();
        data = item.getEncryptedData();
        created = item.getCreated();
        lastModified = item.getLastModified();
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
