package pw.narumi.proton.server.packet;

import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.server.client.Client;

@FunctionalInterface
public interface PacketHandler {

    void packetReceived(final Client client, final Packet packet);
}
