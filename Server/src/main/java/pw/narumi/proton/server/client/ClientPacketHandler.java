package pw.narumi.proton.server.client;

import lombok.Data;
import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.server.packet.incoming.ClientAddPublicKeyPacket;
import pw.narumi.proton.server.packet.incoming.ClientChatPacket;
import pw.narumi.proton.server.packet.incoming.ClientCommandPacket;
import pw.narumi.proton.server.packet.incoming.ConnectUserPacket;
import pw.narumi.proton.server.packet.outgoing.AddPublicKeyPacket;
import pw.narumi.proton.server.packet.outgoing.ResponseMessagePacket;
import pw.narumi.proton.server.packet.outgoing.ServerChatPacket;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

@Data
public class ClientPacketHandler implements PacketHandler {

    private final Client client;

    @Override
    public void packetReceived(final Packet packet) {
        if (!this.client.isLogged()) {
            if (packet instanceof ConnectUserPacket) {
                final ConnectUserPacket userPacket = (ConnectUserPacket) packet;
                this.client.setUsername(userPacket.getUserName());
                this.client.setLogged(true);
            } else {
                this.client.sendPacket(new ResponseMessagePacket("OKE"));
                this.client.close();
            }
            return;
        }

        if (packet instanceof ClientChatPacket) {
            final ClientChatPacket chatPacket = (ClientChatPacket) packet;
            ProtonServer.INSTANCE.getClientManager().sendPacketTo(new ServerChatPacket(this.client.getUsername(), chatPacket.getMessage()), chatPacket.getToUser());
        } else if (packet instanceof ClientCommandPacket) {
            //TODO: DODAC KOMENDY LEL
        } else if (packet instanceof ClientAddPublicKeyPacket) {
            final ClientAddPublicKeyPacket keyPacket = (ClientAddPublicKeyPacket) packet;
            ProtonServer.INSTANCE.getClientManager().sendPacket(new AddPublicKeyPacket(this.client.getUsername(), keyPacket.getPublicKey()));
        }
    }
}
