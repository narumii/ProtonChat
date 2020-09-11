package pw.narumi.proton.network.packet;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private final boolean outgoing;
    private final Map<Integer, Class<? extends Packet>> incomingPackets = new HashMap<>();
    private final Map<Class<? extends Packet>, Integer> outgoingPackets = new HashMap<>();

    public PacketRegistry(final boolean outgoing) {
        this.outgoing = outgoing;
    }

    public void registerPackets(final Class<? extends Packet>... clazz) {
        for (final Class<? extends Packet> aClass : clazz) {
            registerPacket(aClass);
        }
    }

    public void registerPacket(Class<? extends Packet> clazz) {
        if (outgoing) {
            outgoingPackets.put(clazz, outgoingPackets.size() + 1);
            return;
        }

        incomingPackets.put(incomingPackets.size() + 1, clazz);
    }

    public Packet createPacket(final int id) {
        try {
            final Class<? extends Packet> clazz = incomingPackets.get(id);
            return clazz.newInstance();
        } catch (final Throwable throwable) {
            return null;
        }
    }

    public int getPacketID(final Class<? extends Packet> clazz) {
        return outgoingPackets.getOrDefault(clazz, -1);
    }
}
