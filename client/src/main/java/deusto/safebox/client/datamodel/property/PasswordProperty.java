package deusto.safebox.client.datamodel.property;

import deusto.safebox.client.gui.component.PasswordField;

public class PasswordProperty extends MutableItemProperty<String, PasswordField> {

    private final int limit;

    public PasswordProperty(String displayName, int limit, String initialValue) {
        super(displayName, initialValue);
        this.limit = limit;
    }

    @Override
    public void setByComponent(PasswordField component) {
        set(new String(component.getPassword()));
    }

    @Override
    public PasswordField newComponent() {
        return new PasswordField(limit, get(), false);
    }

    @Override
    public String toString() {
        return get();
    }
}
