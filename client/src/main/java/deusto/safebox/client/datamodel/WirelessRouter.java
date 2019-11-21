package deusto.safebox.client.datamodel;

public class WirelessRouter extends Item {

    private String stationName;
    private String stationPassword;
    private String location;

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

    public WirelessRouter(String stationName, String stationPassword, String location) {
        super();
        this.stationName = stationName;
        this.stationPassword = stationPassword;
        this.location = location;
    }



}
