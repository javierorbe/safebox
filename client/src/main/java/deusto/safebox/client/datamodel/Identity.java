package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.DateProperty;
import deusto.safebox.client.datamodel.property.StringProperty;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Identity extends LeafItem {

    private final StringProperty firstName;
    private final StringProperty lastName;
    // TODO: change this to an enum
    private final StringProperty sex;
    private final DateProperty birthDate;
    private final StringProperty address;
    private final StringProperty phone;

    private Identity(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                     String firstName, String lastName, String sex, LocalDate birthDate, String address, String phone) {
        super(id, ItemType.CREDIT_CARD, title, folder, created, lastModified);
        this.firstName = new StringProperty("First Name", 50, firstName);
        this.lastName = new StringProperty("Last Name", 50, lastName);
        this.sex = new StringProperty("Sex", 50, sex);
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
                "", "", "", null, "", "");
    }

    @Override
    JsonObject getCustomData() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public static Identity of(ItemData itemData, Folder folder, JsonObject data) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
