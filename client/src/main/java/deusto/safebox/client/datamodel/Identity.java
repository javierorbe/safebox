package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Identity extends LeafItem{

    private String firstName;
    private String lastName;
    private String sex;
    private LocalDate birthDate;
    private String address;
    private String phone;

    private Identity(UUID id, String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                    String firstName, String lastName, String sex, LocalDate birthDate, String address, String phone) {
        super(id, ItemType.IDENTITY, itemName, folder, created, lastModified);
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.birthDate = birthDate;
        this.address = address;
        this.phone = phone;
        updateFeatures();
    }

    public Identity(String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                    String firstName, String lastName, String sex, LocalDate birthDate, String address, String phone) {
        this(UUID.randomUUID(), itemName, folder, created, lastModified,
                firstName, lastName, sex, birthDate, address, phone);
        updateFeatures();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public Object getProperty(int index) {
        return null;
    }

    @Override
    public void updateFeatures() {
        getFeatures().addAll(new ArrayList<>(Arrays.asList(
                new ItemProperty<>(firstName, "First Name: "),
                new ItemProperty<>(lastName, "Last Name: "),
                new ItemProperty<>(sex, "Sex: "),
                new ItemProperty<>(birthDate, "Birtdate: "),
                new ItemProperty<>(address, "Adress: "),
                new ItemProperty<>(phone, "Phone: ")
        )));
    }

    @Override
    JsonObject getCustomData() {
        return null;
    }
}