package deusto.safebox.client.datamodel.property;

import deusto.safebox.client.gui.component.LimitedTextField;

public class StringProperty extends MutableItemProperty<String, LimitedTextField> {

    private final int limit;

    public StringProperty(String name, int limit, String initialValue) {
        super(name, initialValue);
        this.limit = limit;
    }

    @Override
    public LimitedTextField newComponent() {
        return new LimitedTextField(limit, get(), true);
    }

    @Override
    public void setByComponent(LimitedTextField component) {
        set(component.getText());
    }

    @Override
    public String toString() {
        return get();
    }
}
