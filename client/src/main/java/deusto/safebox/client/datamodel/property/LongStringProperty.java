package deusto.safebox.client.datamodel.property;

import deusto.safebox.client.gui.component.ScrollTextArea;

public class LongStringProperty extends MutableItemProperty<String, ScrollTextArea> {

    public LongStringProperty(String displayName, String initialValue) {
        super(displayName, initialValue);
    }

    @Override
    public ScrollTextArea newComponent() {
        return new ScrollTextArea(get(), 4, 0);
    }

    @Override
    public void setByComponent(ScrollTextArea component) {
        set(component.getTextArea().getText());
    }

    @Override
    public String toString() {
        return get();
    }
}
