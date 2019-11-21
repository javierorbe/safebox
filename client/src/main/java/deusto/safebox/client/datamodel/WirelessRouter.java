package deusto.safebox.client.datamodel;

        import com.google.gson.JsonObject;
        import deusto.safebox.common.ItemType;

        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.UUID;

public class WirelessRouter extends LeafItem {

    private String stationName;
    private String stationPassword;
    private String location;

    private WirelessRouter(UUID id, String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                           String stationName, String stationPassword, String location) {
        super(id, ItemType.WIRELESS_ROUTER, itemName, folder, created, lastModified);
        this.stationName = stationName;
        this.stationPassword = stationPassword;
        this.location = location;
        updateFeatures();
    }

    public WirelessRouter (String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                           String stationName, String stationPassword, String location) {
        this(UUID.randomUUID(), itemName, folder, created, lastModified,
                stationName, stationPassword, location);
        updateFeatures();
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationPassword() {
        return stationPassword;
    }

    public void setStationPassword(String stationPassword) {
        this.stationPassword = stationPassword;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public Object getProperty(int index) {
        return null;
    }

    @Override
    public void updateFeatures() {
        getFeatures().addAll(new ArrayList<>(Arrays.asList(
                new ItemProperty<>(stationName, "Station name: "),
                new ItemProperty<>(stationPassword, "Station password: "),
                new ItemProperty<>(location, "Location")
        )));
    }

    @Override
    JsonObject getCustomData() {
        return null;
    }
}
