package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.util.Constants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Note extends LeafItem {

    private String content;

    private Note(UUID id, String name, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                 String content) {
        super(id, ItemType.NOTE, name, folder, created, lastModified);
        this.content = content;
        getFeatures().addAll(new ArrayList<>(Arrays.asList(
                new ItemProperty<>(content, "Content: ")
        )));
    }

    public Note(String name, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                String content) {
        this(UUID.randomUUID(), name, folder, created, lastModified, content);
    }

    public Note(Folder folder) {
        this("", folder, LocalDateTime.now(), LocalDateTime.now(), "");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
                return getName();
            case 1:
                return content;
            case 2:
                return Constants.DATE_TIME_FORMATTER.format(getCreated());
            case 3:
                return Constants.DATE_TIME_FORMATTER.format(getLastModified());
        }

        throw new IllegalArgumentException("No property with index " + index);
    }
}
