package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.StringProperty;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BankAccount extends LeafItem {

    private final StringProperty holder;
    private final StringProperty iban;
    private final StringProperty bankName;

    private BankAccount(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                        String holder, String iban, String bankName) {
        super(id, ItemType.BANK_ACCOUNT, title, folder, created, lastModified);
        this.holder = new StringProperty("Account Holder", 50, holder);
        this.iban = new StringProperty("IBAN", 50, iban);
        this.bankName = new StringProperty("Bank Name", 50, bankName);
        addProperties(List.of(
                this.holder,
                this.iban,
                this.bankName
        ));
    }

    public BankAccount(Folder folder) {
        this(UUID.randomUUID(), "", folder, LocalDateTime.now(), LocalDateTime.now(),
                "", "", "");
    }

    @Override
    JsonObject getCustomData() {
        JsonObject root = new JsonObject();
        root.addProperty("holder", holder.get());
        root.addProperty("iban", iban.get());
        root.addProperty("bankName", bankName.get());
        return root;
    }

    static BankAccount build(ItemData itemData, Folder folder, JsonObject data) {
        return new BankAccount(
                itemData.getId(),
                data.get("title").getAsString(),
                folder,
                itemData.getCreated(),
                itemData.getLastModified(),
                data.get("holder").getAsString(),
                data.get("iban").getAsString(),
                data.get("bankName").getAsString()
        );
    }
}
