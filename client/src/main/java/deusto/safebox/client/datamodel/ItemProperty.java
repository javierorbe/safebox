package deusto.safebox.client.datamodel;

public class ItemProperty <T, String>{

    private T feature;
    private String name;

    ItemProperty(T feature, String name) {
        this.name = name;
        this.feature = feature;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFeature(T feature) {
        this.feature = feature;
    }

    public String getName() {
        return name;
    }

    public T getFeature() {
        return feature;
    }
}
