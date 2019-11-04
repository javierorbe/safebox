package deusto.safebox.common.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import deusto.safebox.common.net.packet.TestPacket;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class PacketActionTest {

    @Test
    void packetActionTest() {
        PacketAction packetAction = new PacketAction();

        Consumer<TestPacket> testPacketConsumer = testPacket -> {};
        packetAction.putAction(TestPacket.class, testPacketConsumer);
        TestPacket testPacket = new TestPacket("Example");

        assertEquals(testPacketConsumer, packetAction.getAction(testPacket));
    }
}
