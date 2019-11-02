package deusto.safebox.common;

public enum ItemType {

    FOLDER((byte) 0, "Folder"),
    LOGIN((byte) 1, "Login"),
    NOTE((byte) 2, "Note"),
    IDENTITY((byte) 3, "Identity"),
    WIRELESS_ROUTER((byte) 4, "Wireless Router"),
    CREDIT_CARD((byte) 5, "Credit Card"),
    BANK_ACCOUNT((byte) 6, "Bank Account"),
    ;

    private static final ItemType[] ID_MAPPER = new ItemType[ItemType.values().length];

    static {
        for (ItemType type : ItemType.values()) {
            ID_MAPPER[type.getId()] = type;
        }
    }

    private byte id;
    private String name;

    ItemType(byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ItemType fromId(byte id) {
        return ID_MAPPER[id];
    }
}
