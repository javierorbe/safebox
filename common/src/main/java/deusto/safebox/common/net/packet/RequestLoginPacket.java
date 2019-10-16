package deusto.safebox.common.net.packet;

public class RequestLoginPacket extends Packet {

    private String email;
    private String password;

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
        return "RequestLoginPacket";
    }
}
