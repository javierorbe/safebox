package deusto.safebox.client.datamodel;

import java.sql.Date;

public class CreditCard extends Item {

    private String cardholder;
    private String type;
    private String number;
    private Date expireDate;

    public String getCardholder() {
        return cardholder;
    }
    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public Date getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public CreditCard(String cardholder, String type, String number, Date expireDate) {
        super();
        this.cardholder = cardholder;
        this.type = type;
        this.number = number;
        this.expireDate = expireDate;
    }



}
