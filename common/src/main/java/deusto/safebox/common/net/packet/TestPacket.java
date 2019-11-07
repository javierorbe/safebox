package deusto.safebox.common.net.packet;

import java.io.Serializable;

public class TestPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 1681049760090246927L;

    private final String text;

    public TestPacket(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return getClass().getName() +  " (" + text + ")";
    }
}
