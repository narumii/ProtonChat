package pw.narumi.proton.client.client;

import lombok.AllArgsConstructor;
import pw.narumi.proton.client.Bootstrap;
import pw.narumi.proton.client.logger.ChatColor;
import pw.narumi.proton.client.packet.incoming.*;
import pw.narumi.proton.client.packet.outgoing.ClientHandshakePacket;
import pw.narumi.proton.client.packet.outgoing.ClientResponseKeyPacket;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

@AllArgsConstructor
public class ClientPacketHandler implements PacketHandler {

    private final Client client;

    @Override
    public void packetReceived(final Packet packet) {
        if (packet instanceof ServerRequestHandshakePacket) {
            client.sendPacket(new ClientHandshakePacket(client.getUserName()));
        }else if (packet instanceof ServerDisconnectPacket) {
            Bootstrap.LOGGER.info(ChatColor.RED + "Disconnected: " + ChatColor.RESET + ((ServerDisconnectPacket) packet).getMessage());
            Bootstrap.setPrefix("> ");
        } else if (packet instanceof ServerResponseMessagePacket) {
            Bootstrap.LOGGER.info(ChatColor.RED + "Message: " + ChatColor.RESET + ((ServerResponseMessagePacket) packet).getMessage());
        } else {
            if (packet instanceof ServerResponseKeyPacket) {
                final ServerResponseKeyPacket keyPacket = (ServerResponseKeyPacket) packet;
                if (!keyPacket.getUserName().equals(client.getUserName())) {
                    client.getKeys().put(keyPacket.getUserName(), CryptographyHelper.generatePUblicKeyFromString(keyPacket.getPublicKey()));
                }
            } else if (packet instanceof ServerRequestKeyPacket) {
                client.sendPacket(new ClientResponseKeyPacket(client.getUserName(), CryptographyHelper.generateStringFromPublicKey(client.getKeyPair().getPublic())));
            } else if (packet instanceof ServerChatPacket) {
                final ServerChatPacket chatPacket = (ServerChatPacket) packet;
                Bootstrap.LOGGER.info(String.format(ChatColor.DARK_PURPLE + "%s: " + ChatColor.RESET + "%s", chatPacket.getUser(), CryptographyHelper.decodeMessage(chatPacket.getMessage(), client.getKeyPair().getPrivate())));
            }
        }
    }
}
