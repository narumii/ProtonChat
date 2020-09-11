package pw.narumi.proton.shared.packet;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private final boolean outgoing;
    private final Map<Integer, Class<? extends Packet>> incomingPackets;
    private final Map<Class<? extends Packet>, Integer> outgoingPackets;

    public PacketRegistry(final boolean outgoing) {
        this.outgoing = outgoing;
        this.incomingPackets = new HashMap<>();
        this.outgoingPackets = new HashMap<>();
    }

    @SafeVarargs
    public final void registerPackets(final Class<? extends Packet>... clazz) {
        for (final Class<? extends Packet> aClass : clazz) {
            registerPacket(aClass);
        }
    }

    private void registerPacket(Class<? extends Packet> clazz) {
        if (this.outgoing) {
            this.outgoingPackets.put(clazz, this.outgoingPackets.size() + 1);
            return;
        }

        this.incomingPackets.put(this.incomingPackets.size() + 1, clazz);
    }

    public Packet createPacket(final int id) {
        try {
            final Class<? extends Packet> clazz = this.incomingPackets.get(id);
            return clazz.newInstance();
        } catch (final Throwable throwable) {
            return null;
        }
    }

    public int getPacketID(final Class<? extends Packet> clazz) {
        return this.outgoingPackets.getOrDefault(clazz, -1);
    }
}
