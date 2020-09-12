package pw.narumi.proton.client.client;

import pw.narumi.proton.client.Bootstrap;
import pw.narumi.proton.client.ProtonClient;
import pw.narumi.proton.client.logger.ChatColor;
import pw.narumi.proton.client.packet.incoming.*;
import pw.narumi.proton.client.packet.outgoing.ClientHandshakePacket;
import pw.narumi.proton.client.packet.outgoing.ClientRequestKeyPacket;
import pw.narumi.proton.client.packet.outgoing.ClientResponseKeyPacket;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

public class ClientPacketHandler implements PacketHandler {

    @Override
    public void packetReceived(final Packet packet) {
        final Client client = ProtonClient.INSTANCE.getClient();
        if (packet instanceof ServerRequestHandshakePacket) {
            client.sendPacket(new ClientHandshakePacket(client.getUserName()));

            client.sendPacket(new ClientRequestKeyPacket());
            client.sendPacket(new ClientResponseKeyPacket(client.getUserName(), CryptographyHelper.generateStringFromPublicKey(client.getKeyPair().getPublic())));
        }else if (packet instanceof ServerDisconnectPacket) {
            Bootstrap.LOGGER.info(ChatColor.RED + "Disconnected: " + ChatColor.RESET + ((ServerDisconnectPacket) packet).getMessage());
            client.close();
        } else if (packet instanceof ServerResponseMessagePacket) {
            Bootstrap.LOGGER.info(ChatColor.RED + "Message: " + ChatColor.RESET + ((ServerResponseMessagePacket) packet).getMessage());
        } else {
            if (packet instanceof ServerResponseKeyPacket) {
                final ServerResponseKeyPacket keyPacket = (ServerResponseKeyPacket) packet;
                if (!keyPacket.getUserName().equals(client.getUserName()))
                    client.getKeys().put(keyPacket.getUserName(), CryptographyHelper.generatePUblicKeyFromString(keyPacket.getPublicKey()));
            } else if (packet instanceof ServerRequestKeyPacket) {
                client.sendPacket(new ClientResponseKeyPacket(client.getUserName(), CryptographyHelper.generateStringFromPublicKey(client.getKeyPair().getPublic())));
            } else if (packet instanceof ServerChatPacket) {
                final ServerChatPacket chatPacket = (ServerChatPacket) packet;
                Bootstrap.LOGGER.info(String.format(ChatColor.DARK_PURPLE + "%s: " + ChatColor.RESET + "%s", chatPacket.getUser(), CryptographyHelper.decodeMessage(chatPacket.getMessage(), client.getKeyPair().getPrivate())));
            }
        }
    }
}
