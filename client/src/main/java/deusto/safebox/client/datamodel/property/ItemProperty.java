package deusto.safebox.client.datamodel.property;

/**
 * An item property with a value of type {@link T}.
 *
 * @param <T> the type of the property value
 */
public abstract class ItemProperty<T> {

    private final String displayName;
    private T value;

    /**
     * Constructs an item property with the specified display name and initial value.
     *
     * @param displayName the property name
     * @param initialValue the initial value of the property
     */
    ItemProperty(String displayName, T initialValue) {
        this.displayName = displayName;
        value = initialValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Returns the value of the property. */
    public T get() {
        return value;
    }

    /** Sets the value of the property. */
    public void set(T value) {
        this.value = value;
    }

    @Override
    public abstract String toString();
}
