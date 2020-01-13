package deusto.safebox.client.datamodel;

import static java.util.stream.Collectors.toMap;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.DateProperty;
import deusto.safebox.client.datamodel.property.EnumProperty;
import deusto.safebox.client.datamodel.property.StringProperty;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.util.Constants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Identity extends LeafItem {

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final EnumProperty<Sex> sex;
    private final DateProperty birthDate;
    private final StringProperty address;
    private final StringProperty phone;

    private Identity(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                     String firstName, String lastName, Sex sex, LocalDate birthDate, String address, String phone) {
        super(id, ItemType.IDENTITY, title, folder, created, lastModified);
        this.firstName = new StringProperty("First Name", 50, firstName);
        this.lastName = new StringProperty("Last Name", 50, lastName);
        this.sex = new EnumProperty<>("Sex", Sex.values(), sex);
        this.birthDate = new DateProperty("Birth Date", birthDate);
        this.address = new StringProperty("Address", 50, address);
        this.phone = new StringProperty("Phone", 50, phone);
        addProperties(List.of(
                this.firstName,
                this.lastName,
                this.sex,
                this.birthDate
        ));
    }

    public Identity(Folder folder) {
        this(UUID.randomUUID(), "", folder, LocalDateTime.now(), LocalDateTime.now(),
                "", "", Sex.UNSPECIFIED, null, "", "");
    }

    @Override
    JsonObject getCustomData() {
        JsonObject root = new JsonObject();
        root.addProperty("firstName", firstName.get());
        root.addProperty("lastName", lastName.get());
        root.addProperty("sex", sex.get().id);
        root.add("birthDate", Constants.GSON.toJsonTree(birthDate.get()));
        root.addProperty("address", address.get());
        root.addProperty("phone", phone.get());
        return root;
    }

    static Identity build(ItemData itemData, Folder folder, JsonObject data) {
        return new Identity(
                itemData.getId(),
                data.get("title").getAsString(),
                folder,
                itemData.getCreated(),
                itemData.getLastModified(),
                data.get("firstName").getAsString(),
                data.get("lastName").getAsString(),
                Sex.fromId(data.get("sex").getAsByte()),
                Constants.GSON.fromJson(data.get("birthDate"), LocalDate.class),
                data.get("address").getAsString(),
                data.get("phone").getAsString()
        );
    }

    public enum Sex {
        MALE((byte) 0, "Male"),
        FEMALE((byte) 1, "Female"),
        APACHE_HELICOPTER((byte) 2, "Apache Helicopter"),
        UNSPECIFIED((byte) 3, "Unspecified"),
        ;

        private static final Map<Byte, Sex> ID_MAPPER
                = Arrays.stream(Sex.values()).collect(toMap(Sex::getId, e -> e));

        private final byte id;
        private final String name;

        Sex(byte id, String name) {
            this.id = id;
            this.name = name;
        }

        private byte getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }

        private static Sex fromId(byte id) {
            return ID_MAPPER.get(id);
        }
    }
}
