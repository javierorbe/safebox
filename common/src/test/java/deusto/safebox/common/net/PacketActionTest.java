package deusto.safebox.common.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import deusto.safebox.common.net.packet.TestPacket;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class PacketActionTest {

    @Test
    void packetActionTest() {
        PacketAction packetAction = new PacketAction();

        Consumer<TestPacket> testPacketAction = testPacket -> {};
        packetAction.putAction(TestPacket.class, testPacketAction);

        TestPacket testPacket = new TestPacket("Example");
        packetAction.getAction(testPacket).ifPresentOrElse(
            action -> assertEquals(testPacketAction, action),
            () -> fail("There is no action defined for a TestPacket.")
        );
    }
}
