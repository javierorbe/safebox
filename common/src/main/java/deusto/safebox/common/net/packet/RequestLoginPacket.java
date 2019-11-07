package deusto.safebox.common.net.packet;

import java.io.Serializable;

public class RequestLoginPacket extends Packet implements Serializable {

    private static final long serialVersionUID = -6617763103555715051L;

    private final String email;
    private final String password;

    public RequestLoginPacket(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return getClass().getName() + " (" + email + ")";
    }
}
