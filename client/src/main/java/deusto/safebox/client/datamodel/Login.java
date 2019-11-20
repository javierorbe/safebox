package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.util.Constants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Login extends LeafItem {

    private String username;
    private String password;
    private String website;
    private LocalDate passwordExpiration;

    private Login(UUID id, String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                  String username, String password, String website, LocalDate passwordExpiration) {
        super(id, ItemType.LOGIN, itemName, folder, created, lastModified);
        this.username = username;
        this.password = password;
        this.website = website;
        this.passwordExpiration = passwordExpiration;
        setFeatures(new ArrayList<>(Arrays.asList(
                new ItemProperty<>(username, "Username: "),
                new ItemProperty<>(password, "Password: "),
                new ItemProperty<>(website, "Website: "),
                new ItemProperty<>(passwordExpiration, "Password expiration: ")
        )));
    }

    public Login(String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                 String username, String password, String website, LocalDate passwordExpiration) {
        this(UUID.randomUUID(), itemName, folder, created, lastModified,
                username, password, website, passwordExpiration);
    }

    public Login(Folder folder) {
        this("", folder, LocalDateTime.now(), LocalDateTime.now(), "", "", "", null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public LocalDate getPasswordExpiration() {
        return passwordExpiration;
    }

    public void setPasswordExpiration(LocalDate passwordExpiration) {
        this.passwordExpiration = passwordExpiration;
    }

    @Override
    protected JsonObject getCustomData() {
        JsonObject root = new JsonObject();
        root.addProperty("username", username);
        root.addProperty("password", password);
        root.addProperty("website", website);
        root.add("passwordExpiration", Constants.GSON.toJsonTree(passwordExpiration));
        return root;
    }

    @Override
    public Object getProperty(int index) {
        switch (index) {
            case 0:
                return getName();
            case 1:
                return username;
            case 2:
                return password;
            case 3:
                return website;
            case 4:
                return Constants.DATE_TIME_FORMATTER.format(getCreated());
            case 5:
                return Constants.DATE_TIME_FORMATTER.format(getLastModified());
        }

        throw new IllegalArgumentException("No property with index " + index);
    }
}
