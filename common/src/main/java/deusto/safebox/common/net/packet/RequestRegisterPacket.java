package deusto.safebox.common.net.packet;

import java.io.Serializable;

public class RequestRegisterPacket extends Packet implements Serializable {

    private static final long serialVersionUID = -7581124302981646793L;

    private final String name;
    private final String email;
    private final String password;

    public RequestRegisterPacket(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + email + ")";
    }
}
