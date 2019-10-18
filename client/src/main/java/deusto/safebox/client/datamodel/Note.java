package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.util.Constants;
import java.time.LocalDateTime;

public class Note extends LeafItem {

    private String content;

    public Note(String name, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                String content) {
        super(name, folder, created, lastModified);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public ItemType getItemType() {
        return ItemType.NOTE;
    }

    @Override
    protected JsonObject getCustomData() {
        JsonObject root = new JsonObject();
        root.addProperty("content", content);
        return root;
    }

    @Override
    public Object getProperty(int index) {
        switch (index) {
            case 0:
                return getItemName();
            case 1:
                return content;
            case 2:
                return Constants.DATE_TIME_FORMATTER.format(getCreated());
            case 3:
                return Constants.DATE_TIME_FORMATTER.format(getLastModified());
            default:
                return "";
        }
    }
}
