package deusto.safebox.common.net;

public class TestPacket extends Packet {

    private String text;

    public TestPacket(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TestPacket: " + text;
    }
}
