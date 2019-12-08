package deusto.safebox.common.net.packet;

/** Packet containing the information to log in to the app. */
public class RequestLoginPacket extends Packet {

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
        return getClass().getSimpleName() + " (" + email + ")";
    }
}
