package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class BankAccount extends LeafItem{

    private String accountHolder;
    private String dniTitular;
    private String iban;
    private String bankName;

    private BankAccount(UUID id, String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                       String accountHolder, String dniTitular, String iban, String bankName) {
        super(id, ItemType.BANK_ACCOUNT, itemName, folder, created, lastModified);
        this.accountHolder = accountHolder;
        this.dniTitular = dniTitular;
        this.iban = iban;
        this.bankName = bankName;
        updateFeatures();
    }

    public BankAccount(String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                       String accountHolder, String dniTitular, String iban, String bankName) {
        this(UUID.randomUUID(), itemName, folder, created, lastModified,
                accountHolder, dniTitular, iban, bankName);
        updateFeatures();
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getDniTitular() {
        return dniTitular;
    }

    public void setDniTitular(String dniTitular) {
        this.dniTitular = dniTitular;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public Object getProperty(int index) {
        return null;
    }

    @Override
    public void updateFeatures() {
        getFeatures().addAll(new ArrayList<>(Arrays.asList(
                new ItemProperty<>(accountHolder, "Account holder: "),
                new ItemProperty<>(dniTitular, "DNI titular: "),
                new ItemProperty<>(iban, "IBAN: "),
                new ItemProperty<>(bankName, "Bank name: ")
        )));
    }

    @Override
    JsonObject getCustomData() {
        return null;
    }
}