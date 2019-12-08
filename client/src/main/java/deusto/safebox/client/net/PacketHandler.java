package deusto.safebox.client.net;

import deusto.safebox.client.util.event.EventHandler;
import deusto.safebox.client.util.event.EventListener;
import deusto.safebox.client.util.event.Listener;
import deusto.safebox.common.net.packet.ErrorPacket;
import deusto.safebox.common.net.packet.Packet;

/**
 * Handles the received packets.
 * Singleton.
 */
public class PacketHandler extends EventHandler<Packet> implements Listener {

    public static final PacketHandler INSTANCE = new PacketHandler();

    private PacketHandler() {
        registerListeners(this);
    }

    @EventListener
    private static void onError(ErrorPacket packet) {
        ErrorHandler.fire(packet.getErrorType());
    }
}
