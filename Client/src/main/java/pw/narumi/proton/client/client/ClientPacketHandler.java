package pw.narumi.proton.client.client;

import lombok.Data;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

@Data
public class ClientPacketHandler implements PacketHandler {

    @Override
    public void packetReceived(final Packet packet) {

    }
}
