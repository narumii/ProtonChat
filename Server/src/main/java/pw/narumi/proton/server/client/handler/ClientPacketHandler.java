package pw.narumi.proton.server.client.handler;

import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.server.client.Client;
import pw.narumi.proton.server.packet.PacketHandler;
import pw.narumi.proton.server.packet.incoming.ClientAddPublicKeyPacket;
import pw.narumi.proton.server.packet.incoming.ClientChatPacket;
import pw.narumi.proton.server.packet.incoming.ClientCommandPacket;
import pw.narumi.proton.server.packet.incoming.ConnectUserPacket;
import pw.narumi.proton.server.packet.outgoing.AddPublicKeyPacket;
import pw.narumi.proton.server.packet.outgoing.ResponseMessagePacket;
import pw.narumi.proton.server.packet.outgoing.ServerChatPacket;
import pw.narumi.proton.shared.packet.Packet;

public class ClientPacketHandler implements PacketHandler {

    //FIXME: RED NAPRAW TO XD
    @Override
    public void packetReceived(final Client client, final Packet packet) {
        if (!(packet instanceof ConnectUserPacket) && !client.isLogged()) {
            client.sendPacket(new ResponseMessagePacket("OKE"));
            client.close();
        }else if (packet instanceof ConnectUserPacket && !client.isLogged()) {
            final ConnectUserPacket userPacket = (ConnectUserPacket) packet;
            client.setUsername(userPacket.getUserName());
            client.setUserID(userPacket.getUserID());
            client.setLogged(true);
        }else {
            if (packet instanceof ClientChatPacket) {
                final ClientChatPacket chatPacket = (ClientChatPacket) packet;
                if (chatPacket.getUserID() == client.getUserID())
                    ProtonServer.INSTANCE.getClientManager().sendPacketTo(new ServerChatPacket(client.getUsername(), chatPacket.getMessage()), chatPacket.getToUser());
            }else if (packet instanceof ClientCommandPacket) {
                //TODO: DODAC KOMENDY LEL
            }else if (packet instanceof ClientAddPublicKeyPacket) {
                final ClientAddPublicKeyPacket keyPacket = (ClientAddPublicKeyPacket) packet;
                if (keyPacket.getUserID() == client.getUserID())
                    ProtonServer.INSTANCE.getClientManager().sendPacket(new AddPublicKeyPacket(client.getUsername(), keyPacket.getPublicKey()));
            }
        }
    }
}
