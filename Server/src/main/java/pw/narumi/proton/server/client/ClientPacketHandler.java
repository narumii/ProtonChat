package pw.narumi.proton.server.client;

import lombok.AllArgsConstructor;
import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.server.packet.incoming.*;
import pw.narumi.proton.server.packet.outgoing.*;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

@AllArgsConstructor
public class ClientPacketHandler implements PacketHandler {

    private final Client client;

    @Override
    public void packetReceived(final Packet packet) {
        if (!this.client.isLogged()) {
            if (packet instanceof ClientHandshakePacket) {
                final ClientHandshakePacket handshakePacket = (ClientHandshakePacket) packet;

                if (ProtonServer.INSTANCE.getClientManager().clientExists(handshakePacket.getUserName())){
                    this.client.sendPacket(new ServerDisconnectPacket("Another user uses this name."));
                    this.client.close();
                }
                if (!ProtonServer.INSTANCE.getClientManager().isNickValid(handshakePacket.getUserName())) {
                    this.client.sendPacket(new ServerDisconnectPacket("Your nick is invalid. (Max nick length: 16, you can't use any unicode char or space)"));
                    this.client.close();
                }

                this.client.setUsername(handshakePacket.getUserName());
                this.client.setLogged(true);
                System.out.println(String.format("New user connected: [%s]", handshakePacket.getUserName()));
                ProtonServer.INSTANCE.getClientManager().sendPacket(new ServerResponseMessagePacket(String.format("New user connected: [%s]", handshakePacket.getUserName())));
                ProtonServer.INSTANCE.getClientManager().sendPacketBesides(new ServerRequestKeyPacket(), client.getChannel());
            } else {
                this.client.sendPacket(new ServerDisconnectPacket("Yeah yeah XD"));
                this.client.close();
            }
            return;
        }

        if (packet instanceof ClientChatPacket) {
            final ClientChatPacket chatPacket = (ClientChatPacket) packet;
            ProtonServer.INSTANCE.getClientManager().sendPacketTo(new ServerChatPacket(this.client.getUsername(), chatPacket.getMessage()), chatPacket.getToUser());
        } else if (packet instanceof ClientCommandPacket) {
            //TODO: DODAC KOMENDY LEL
        } else if (packet instanceof ClientRequestKeyPacket) {
            ProtonServer.INSTANCE.getClientManager().sendPacket(new ServerRequestKeyPacket());
        } else if (packet instanceof ClientResponseKeyPacket) {
            final ClientResponseKeyPacket keyPacket = (ClientResponseKeyPacket) packet;
            ProtonServer.INSTANCE.getClientManager().sendPacket(new ServerResponseKeyPacket(keyPacket.getUserName(), keyPacket.getPublicKey()));
        }
    }
}
