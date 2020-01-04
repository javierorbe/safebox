package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.StringProperty;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class WirelessRouter extends LeafItem {

    private final StringProperty stationName;
    private final StringProperty stationPassword;
    // TODO: maybe change this to an enum { HOME, WORK, PUBLIC }
    private final StringProperty location;

    private WirelessRouter(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                           String stationName, String stationPassword, String location) {
        super(id, ItemType.WIRELESS_ROUTER, title, folder, created, lastModified);
        this.stationName = new StringProperty("Station Name", 50, stationName);
        this.stationPassword = new StringProperty("Station Password", 50, stationPassword);
        this.location = new StringProperty("Location", 50, location);
        addProperties(List.of(
                this.stationName,
                this.stationPassword,
                this.location
        ));
    }

    public WirelessRouter(Folder folder) {
        this(UUID.randomUUID(), "", folder, LocalDateTime.now(), LocalDateTime.now(),
                "", "", "");
    }

    @Override
    JsonObject getCustomData() {
        JsonObject root = new JsonObject();
        root.addProperty("stationName", stationName.get());
        root.addProperty("stationPassword", stationPassword.get());
        root.addProperty("location", location.get());
        return root;
    }

    static WirelessRouter build(ItemData itemData, Folder folder, JsonObject data) {
        return new WirelessRouter(
                itemData.getId(),
                data.get("title").getAsString(),
                folder,
                itemData.getCreated(),
                itemData.getLastModified(),
                data.get("stationName").getAsString(),
                data.get("stationPassword").getAsString(),
                data.get("location").getAsString()
        );
    }
}
