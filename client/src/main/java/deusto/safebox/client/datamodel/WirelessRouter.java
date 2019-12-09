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
        super(id, ItemType.CREDIT_CARD, title, folder, created, lastModified);
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
        // TODO
        throw new UnsupportedOperationException();
    }

    static WirelessRouter build(ItemData itemData, Folder folder, JsonObject data) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
