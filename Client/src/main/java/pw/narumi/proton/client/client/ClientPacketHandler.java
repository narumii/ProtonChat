package pw.narumi.proton.client.client;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;
import pw.narumi.proton.client.Bootstrap;
import pw.narumi.proton.client.logger.ChatColor;
import pw.narumi.proton.client.packet.incoming.ServerChatPacket;
import pw.narumi.proton.client.packet.incoming.ServerDisconnectPacket;
import pw.narumi.proton.client.packet.incoming.ServerRequestHandshakePacket;
import pw.narumi.proton.client.packet.incoming.ServerRequestKeyPacket;
import pw.narumi.proton.client.packet.incoming.ServerResponseMessagePacket;
import pw.narumi.proton.client.packet.outgoing.ClientHandshakePacket;
import pw.narumi.proton.client.packet.outgoing.ClientResponseKeyPacket;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

@AllArgsConstructor
public class ClientPacketHandler implements PacketHandler {

    private final Client client;

    @Override
    public void packetReceived(final Packet packet) {
        if (packet instanceof ServerRequestHandshakePacket) {
            this.client.sendPacket(new ClientHandshakePacket(client.getUserName()));
        }else if (packet instanceof ServerDisconnectPacket) {
            Bootstrap.LOGGER.info(ChatColor.RED + "Disconnected: " + ChatColor.RESET + ((ServerDisconnectPacket) packet).getMessage());
            Bootstrap.setPrefix("> ");
        } else if (packet instanceof ServerResponseMessagePacket) {
            Bootstrap.LOGGER.info(ChatColor.RED + "Message: " + ChatColor.RESET + ((ServerResponseMessagePacket) packet).getMessage());
        } else if (packet instanceof ServerRequestKeyPacket) {
            try {
                final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(128);
                final SecretKey secretKey = keyGenerator.generateKey();

                this.client.sendPacket(new ClientResponseKeyPacket(((ServerRequestKeyPacket) packet).getPublicKey(), secretKey));
                this.client.setSecretKey(secretKey);
            } catch (final Exception ex) {
                this.client.close();
                throw new RuntimeException("Can't generate and send secret key!", ex.getCause());
            }
        } else if (packet instanceof ServerChatPacket) {
            final ServerChatPacket chatPacket = (ServerChatPacket) packet;
            Bootstrap.LOGGER.info(String.format(ChatColor.DARK_PURPLE + "%s: " + ChatColor.RESET + "%s", chatPacket.getUser(), chatPacket.getMessage()));
        }
    }
}
