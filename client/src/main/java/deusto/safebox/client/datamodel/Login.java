package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.common.ItemType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Login extends LeafItem {

    private String username;
    private String password;
    private String website;

    private LocalDate passwordExpiration;

    public Login(String itemName, Folder folder, LocalDateTime lastModified, LocalDateTime created,
                 String username, String password, String website) {
        super(itemName, folder, lastModified, created);
        this.username = username;
        this.password = password;
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getWebsite() {
        return website;
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
        return root;
    }
}
