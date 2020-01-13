package deusto.safebox.client.datamodel;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.property.DateProperty;
import deusto.safebox.client.datamodel.property.PasswordProperty;
import deusto.safebox.client.datamodel.property.StringProperty;
import deusto.safebox.client.locale.Message;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.util.Constants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Login extends LeafItem {

    private final StringProperty username;
    private final PasswordProperty password;
    private final StringProperty website;
    private final DateProperty expiration;

    // TODO: custom website property with a clickable component that redirects to the website

    private Login(UUID id, String title, Folder folder, LocalDateTime created, LocalDateTime lastModified,
                  String username, String password, String website, LocalDate expiration) {
        super(id, ItemType.LOGIN, title, folder, created, lastModified);
        this.username = new StringProperty(Message.USERNAME.get(), 50, username);
        this.password = new PasswordProperty(Message.PASSWORD.get(), 100, password);
        this.website = new StringProperty(Message.WEBSITE.get(), 200, website);
        this.expiration = new DateProperty(Message.EXPIRATION.get(), expiration);
        addProperties(List.of(
                this.username,
                this.password,
                this.website,
                this.expiration
        ));
    }

    public Login(Folder folder) {
        this(UUID.randomUUID(), "", folder, LocalDateTime.now(), LocalDateTime.now(),
                "", "", "", null);
    }

    @Override
    JsonObject getCustomData() {
        JsonObject root = new JsonObject();
        root.addProperty("username", username.get());
        root.addProperty("password", password.get());
        root.addProperty("website", website.get());
        root.add("expiration", Constants.GSON.toJsonTree(expiration.get()));
        return root;
    }

    static Login build(ItemData itemData, Folder folder, JsonObject data) {
        return new Login(
                itemData.getId(),
                data.get("title").getAsString(),
                folder,
                itemData.getCreated(),
                itemData.getLastModified(),
                data.get("username").getAsString(),
                data.get("password").getAsString(),
                data.get("website").getAsString(),
                Constants.GSON.fromJson(data.get("expiration"), LocalDate.class)
        );
    }
}
