package deusto.safebox.common;

import java.time.LocalDateTime;
import java.util.UUID;

public class EncryptedItem extends AbstractItem {

    private UUID userId;
    private ItemType type;
    /** Encrypted data. */
    private String data;

    public EncryptedItem(UUID id, UUID userId, ItemType type, String data,
                         LocalDateTime created, LocalDateTime lastModified) {
        super(created, lastModified);
        setItemId(id);
        this.userId = userId;
        this.type = type;
        this.data = data;
    }

    @Override
    public ItemType getItemType() {
        return type;
    }

    @Override
    public String getEncryptedData() {
        return data;
    }

    @Override
    public String toString() {
        return getItemId() + " : " + getItemType();
    }

    public UUID getUserId() {
        return userId;
    }
}
