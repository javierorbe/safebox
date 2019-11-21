package deusto.safebox.client.datamodel.property;

/**
 * An item property of type {@link T}.
 *
 * @param <T> the type of the property.
 */
public abstract class ItemProperty<T> {

    private final String displayName;
    private T value;

    /**
     * Creates an item property.
     *
     * @param displayName the property name.
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
