package deusto.safebox.server;

import java.time.LocalDate;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private LocalDate creation;

    public User(UUID id, String name, String email, String password, LocalDate creation) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.creation = creation;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getCreation() {
        return creation;
    }
}
