package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.StringProperty;
import deusto.safebox.client.locale.Message;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BankAccount extends LeafItem {

    private final StringProperty accountHolder;
    private final StringProperty iban;
    private final StringProperty bankName;

    private BankAccount(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                        String accountHolder, String iban, String bankName) {
        super(id, ItemType.BANK_ACCOUNT, title, folder, created, lastModified);
        this.accountHolder = new StringProperty(Message.ACCOUNT_HOLDER.get(), 50, accountHolder);
        this.iban = new StringProperty("IBAN", 34, iban);
        this.bankName = new StringProperty(Message.BANK_NAME.get(), 30, bankName);
        addProperties(List.of(
                this.accountHolder,
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
        root.addProperty("holder", accountHolder.get());
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
