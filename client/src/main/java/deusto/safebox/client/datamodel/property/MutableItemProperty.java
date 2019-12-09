package deusto.safebox.client.datamodel.property;

import javax.swing.JComponent;

/**
 * An {@link ItemProperty} that can be modified using a {@link JComponent}.
 *
 * @param <T> the type of the property value
 * @param <C> the type of the component that is used to modify the property value
 */
public abstract class MutableItemProperty<T, C extends JComponent> extends ItemProperty<T> {

    MutableItemProperty(String displayName, T initialValue) {
        super(displayName, initialValue);
    }

    /**
     * Sets the value of the property to the value in the component.
     *
     * @param component the component
     */
    public abstract void setByComponent(C component);

    /**
     * Returns a new component that is initialized with the current value of the property.
     *
     * @return the component
     */
    public abstract C newComponent();
}
