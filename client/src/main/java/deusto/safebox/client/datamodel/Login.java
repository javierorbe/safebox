package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.security.Encryption;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.util.Constants;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Login extends LeafItem {

    private String username;
    private String password;
    private String website;

    private LocalDate passwordExpiration;

    public Login(String itemName, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                 String username, String password, String website, LocalDate passwordExpiration) {
        super(itemName, folder, created, lastModified);
        this.username = username;
        this.password = password;
        this.website = website;
        this.passwordExpiration = passwordExpiration;
    }

    private Login(String itemName, LocalDateTime created, LocalDateTime lastModified,
                 String username, String password, String website, LocalDate passwordExpiration) {
        this(itemName, null, created, lastModified, username, password, website, passwordExpiration);
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
    public ItemType getItemType() {
        return ItemType.LOGIN;
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
            default:
                return "";
        }
    }
}
