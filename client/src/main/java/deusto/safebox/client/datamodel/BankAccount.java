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
        super(id, ItemType.CREDIT_CARD, title, folder, created, lastModified);
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
        // TODO
        throw new UnsupportedOperationException();
    }

    static BankAccount build(ItemData itemData, Folder folder, JsonObject data) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
