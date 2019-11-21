package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class CreditCard extends LeafItem{

    private String cardHolder;
    private String typeCard;
    private String number;
    private LocalDate expireDate;

    private CreditCard(UUID id, String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                       String cardHolder, String typeCard, String number, LocalDate expireDate) {
        super(id, ItemType.CREDIT_CARD, itemName, folder, created, lastModified);
        this.cardHolder = cardHolder;
        this.typeCard = typeCard;
        this.number = number;
        this.expireDate = expireDate;
        updateFeatures();
    }

    public CreditCard(String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                      String cardHolder, String typeCard, String number, LocalDate expireDate) {
        this(UUID.randomUUID(), itemName, folder, created, lastModified,
                cardHolder, typeCard, number, expireDate);
        updateFeatures();
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(String typeCard) {
        this.typeCard = typeCard;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public Object getProperty(int index) {
        return null;
    }

    @Override
    public void updateFeatures() {
        getFeatures().addAll(new ArrayList<>(Arrays.asList(
                new ItemProperty<>(cardHolder, "Card holder: "),
                new ItemProperty<>(typeCard, "Type card: "),
                new ItemProperty<>(number, "Number: "),
                new ItemProperty<>(expireDate, "Expire date: ")
        )));
    }

    @Override
    JsonObject getCustomData() {
        return null;
    }
}