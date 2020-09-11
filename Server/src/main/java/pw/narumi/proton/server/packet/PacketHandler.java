package pw.narumi.proton.server.packet;

import pw.narumi.proton.network.packet.Packet;
import pw.narumi.proton.server.client.Client;

public abstract class PacketHandler {

    public abstract void packetReceived(final Client client, final Packet packet);
}
