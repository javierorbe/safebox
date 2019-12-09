package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.LongStringProperty;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Note extends LeafItem {

    private final LongStringProperty content;

    private Note(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                 String content) {
        super(id, ItemType.NOTE, title, folder, created, lastModified);
        this.content = new LongStringProperty("Content", content);
        addProperties(List.of(this.content));
    }

    public Note(Folder folder) {
        this(UUID.randomUUID(), "", folder, LocalDateTime.now(), LocalDateTime.now(),
                "");
    }

    @Override
    JsonObject getCustomData() {
        JsonObject root = new JsonObject();
        root.addProperty("content", content.get());
        return root;
    }

    static Note build(ItemData itemData, Folder folder, JsonObject data) {
        return new Note(
                itemData.getId(),
                data.get("title").getAsString(),
                folder,
                itemData.getCreated(),
                itemData.getLastModified(),
                data.get("content").getAsString()
        );
    }
}
