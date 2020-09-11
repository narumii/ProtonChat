package pw.narumi.proton.shared.packet;

@FunctionalInterface
public interface PacketHandler {

    void packetReceived(final Packet packet);
}
