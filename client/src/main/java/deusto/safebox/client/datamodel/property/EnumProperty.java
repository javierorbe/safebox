package deusto.safebox.client.datamodel.property;

import javax.swing.JComboBox;

public class EnumProperty<E extends Enum<E>> extends MutableItemProperty<E, JComboBox<E>> {

    private final E[] values;

    public EnumProperty(String name, E[] values, E initialValue) {
        super(name, initialValue);
        this.values = values;
    }

    @Override
    public void setByComponent(JComboBox<E> component) {
        @SuppressWarnings("unchecked")
        E e = (E) component.getSelectedItem();
        set(e);
    }

    @Override
    public JComboBox<E> newComponent() {
        return new JComboBox<>(values);
    }

    @Override
    public String toString() {
        return get().toString();
    }
}
