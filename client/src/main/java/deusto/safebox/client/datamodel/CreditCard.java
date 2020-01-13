package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.DateProperty;
import deusto.safebox.client.datamodel.property.StringProperty;
import deusto.safebox.client.locale.Message;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.util.Constants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CreditCard extends LeafItem {

    // TODO: create card type enum

    private final StringProperty cardHolder;
    // TODO: create custom property for card type (an enum-like type)
    private final StringProperty type;
    private final StringProperty number;
    private final DateProperty expiration;

    private CreditCard(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                       String cardHolder, String type, String number, LocalDate expiration) {
        super(id, ItemType.CREDIT_CARD, title, folder, created, lastModified);
        this.cardHolder = new StringProperty(Message.CARD_HOLDER.get(), 50, cardHolder);
        this.type = new StringProperty(Message.TYPE.get(), 50, type);
        this.number = new StringProperty(Message.NUMBER.get(), 50, number);
        this.expiration = new DateProperty(Message.EXPIRATION.get(), expiration);
        addProperties(List.of(
                this.cardHolder,
                this.type,
                this.number,
                this.expiration
        ));
    }

    public CreditCard(Folder folder) {
        this(UUID.randomUUID(), "", folder, LocalDateTime.now(), LocalDateTime.now(),
                "", "", "", null);
    }

    @Override
    JsonObject getCustomData() {
        JsonObject root = new JsonObject();
        root.addProperty("holder", cardHolder.get());
        root.addProperty("type", type.get());
        root.addProperty("number", number.get());
        root.add("expiration", Constants.GSON.toJsonTree(expiration.get()));
        return root;
    }

    static CreditCard build(ItemData itemData, Folder folder, JsonObject data) {
        return new CreditCard(
                itemData.getId(),
                data.get("title").getAsString(),
                folder,
                itemData.getCreated(),
                itemData.getLastModified(),
                data.get("holder").getAsString(),
                data.get("type").getAsString(),
                data.get("number").getAsString(),
                Constants.GSON.fromJson(data.get("expiration"), LocalDate.class)
        );
    }
}
