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

public class CreditCard extends LeafItem {

    // TODO: create card type enum

    private final StringProperty holder;
    // TODO: create custom property for card type (an enum-like type)
    private final StringProperty type;
    private final StringProperty number;
    private final DateProperty expiring;

    private CreditCard(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                       String holder, String type, String number, LocalDate expiring) {
        super(id, ItemType.CREDIT_CARD, title, folder, created, lastModified);
        this.holder = new StringProperty("Card Holder", 50, holder);
        this.type = new StringProperty("Type", 50, type);
        this.number = new StringProperty("Number", 50, number);
        this.expiring = new DateProperty("Expiring", expiring);
        addProperties(List.of(
                this.holder,
                this.type,
                this.number,
                this.expiring
        ));
    }

    public CreditCard(Folder folder) {
        this(UUID.randomUUID(), "", folder, LocalDateTime.now(), LocalDateTime.now(),
                "", "", "", null);
    }

    @Override
    JsonObject getCustomData() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public static CreditCard of(ItemData itemData, Folder folder, JsonObject data) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
