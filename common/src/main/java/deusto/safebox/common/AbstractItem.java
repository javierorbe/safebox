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

    public abstract String getEncryptedData();

    /*
    public JsonObject serialize() {
        JsonObject root = new JsonObject();
        root.addProperty("id", getItemId().toString());
        root.addProperty("type", getItemType().getId());
        root.addProperty("creation_date", Constants.DATE_TIME_FORMATTER.format(getCreated()));
        root.addProperty("last_modified", Constants.DATE_TIME_FORMATTER.format(getLastModified()));
        root.addProperty("data", getEncryptedData());
        return root;
    }
    */
}
