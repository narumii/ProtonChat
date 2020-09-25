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
                    this.client.sendPacket(new ServerDisconnectPacket("Someone already uses this nickname."));
                    this.client.close();
                    return;
                }

                if (!ProtonServer.INSTANCE.getClientManager().isNickValid(handshakePacket.getUserName())) {
                    this.client.sendPacket(new ServerDisconnectPacket("Your nick is invalid. (Max nick length: 16, you can't use unicode chars or spaces)"));
                    this.client.close();
                    return;
                }

                this.client.setUserName(handshakePacket.getUserName());
                this.client.setLogged(true);
                this.client.sendPacket(new ServerRequestKeyPacket());
                System.out.println(String.format("New user connected: [%s]", this.client.getUserName()));
                ProtonServer.INSTANCE.getClientManager().sendPacketBesides(new ServerResponseMessagePacket(String.format("New user connected: [%s]", this.client.getUserName())), this.client.getChannel());
            } else {
                this.client.sendPacket(new ServerDisconnectPacket("Yeah yeah XD"));
                this.client.close();
            }
            return;
        }

        if (packet instanceof ClientChatPacket) {
            final ClientChatPacket chatPacket = (ClientChatPacket) packet;
            ProtonServer.INSTANCE.getClientManager().sendPacketBesides(new ServerChatPacket(this.client.getUserName(), chatPacket.getMessage()), this.client.getChannel());
        } else if (packet instanceof ClientCommandPacket) {
            //TODO: DODAC KOMENDY LEL
        } else if (packet instanceof ClientResponseKeyPacket) {
            this.client.setSecretKey(((ClientResponseKeyPacket) packet).getSecretKey());
        }
    }
}
