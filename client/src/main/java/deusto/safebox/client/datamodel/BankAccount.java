package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;

public class BankAccount extends Item {

    private String accountHolder;
    private String dniTitular;
    private String iban;
    private String bankName;

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

    public BankAccount(String accountHolder, String dniTitular, String iban, String bankName) {
        super();
        this.accountHolder = accountHolder;
        this.dniTitular = dniTitular;
        this.iban = iban;
        this.bankName = bankName;
    }

    @Override
    protected JsonObject getCustomData() {
        // TODO Auto-generated method stub
        return null;
    }
}