package deusto.safebox.common.net.packet;

import java.io.Serializable;

/** Super class for all the packet types. */
public abstract class Packet implements Serializable {

    private static final long serialVersionUID = -9109769836835349878L;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
