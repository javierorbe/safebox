package deusto.safebox.common;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;

public enum ItemType {

    FOLDER((byte) 0, "Folder"),
    LOGIN((byte) 1, "Login"),
    NOTE((byte) 2, "Note"),
    IDENTITY((byte) 3, "Identity"),
    WIRELESS_ROUTER((byte) 4, "Wireless Router"),
    CREDIT_CARD((byte) 5, "Credit Card"),
    BANK_ACCOUNT((byte) 6, "Bank Account"),
    ;

    private static final Map<Byte, ItemType> ID_MAPPER
            = Arrays.stream(values()).collect(toMap(ItemType::getId, e -> e));

    private final byte id;
    private final String name;

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
        return ID_MAPPER.get(id);
    }
}
