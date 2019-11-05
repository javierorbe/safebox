package deusto.safebox.common.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import deusto.safebox.common.net.packet.TestPacket;
import deusto.safebox.common.util.ClassConsumerMap;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class ClassConsumerMapTest {

    @Test
    void test() {
        ClassConsumerMap packetAction = new ClassConsumerMap();

        Consumer<TestPacket> testPacketAction = testPacket -> {};
        packetAction.put(TestPacket.class, testPacketAction);

        TestPacket testPacket = new TestPacket("Example");
        packetAction.get(testPacket).ifPresentOrElse(
            action -> assertEquals(testPacketAction, action),
            () -> fail("There is no action defined for a TestPacket.")
        );
    }
}
