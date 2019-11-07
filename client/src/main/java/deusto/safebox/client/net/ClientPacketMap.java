package deusto.safebox.client.net;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.net.packet.ErrorPacket;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.ReceiveDataPacket;
import deusto.safebox.common.util.BoundClassConsumerMap;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ClientPacketMap extends BoundClassConsumerMap<Packet> {

    public ClientPacketMap() {
        put(ErrorPacket.class, this::onError);
        put(ReceiveDataPacket.class, this::onReceiveData);
    }

    private void onError(ErrorPacket packet) {
        // TODO
    }

    private void onReceiveData(ReceiveDataPacket packet) {
        Map<ItemType, List<ItemData>> rawItems = classifyByType(packet.getItems());
        // TODO
    }

    private static Map<ItemType, List<ItemData>> classifyByType(Collection<ItemData> items) {
        return items.parallelStream()
                .collect(groupingBy(
                    ItemData::getType,
                    () -> new EnumMap<>(ItemType.class),
                    toList())
                );
    }
}
