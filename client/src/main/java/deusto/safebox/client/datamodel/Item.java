package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.AbstractItem;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Item extends AbstractItem {

    private String name;

    private Item(UUID id, String name, LocalDateTime created, LocalDateTime lastModified) {
        super(created, lastModified);
        setItemId(id);
        this.name = name;
    }

    Item(String name, LocalDateTime created, LocalDateTime lastModified) {
        this(null, name, created, lastModified);
    }

    public String getItemName() {
        return name;
    }

    /**
     * Creates and returns a {@link JsonObject} containing the properties
     * that are specific to the item type.
     *
     * @return a {@link JsonObject} with the data.
     */
    protected abstract JsonObject getCustomData();

    @Override
    public String getEncryptedData() {
        JsonObject root = getCustomData();
        root.addProperty("name", name);
        // TODO: encrypt data
        String encryptedData = root.toString();
        return encryptedData;
    }
}
