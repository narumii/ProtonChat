package pw.narumi.proton.client.client;

import pw.narumi.proton.client.Bootstrap;
import pw.narumi.proton.client.ProtonClient;
import pw.narumi.proton.client.packet.incoming.AddPublicKeyPacket;
import pw.narumi.proton.client.packet.incoming.DisconnectPacket;
import pw.narumi.proton.client.packet.incoming.ResponseMessagePacket;
import pw.narumi.proton.client.packet.incoming.ServerChatPacket;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

public class ClientPacketHandler implements PacketHandler {

    protected Client client = ProtonClient.INSTANCE.getClient();

    @Override
    public void packetReceived(final Packet packet) {
        if (packet instanceof DisconnectPacket) {
            Bootstrap.LOGGER.info("$red$Disconnected: $r$" + ((DisconnectPacket) packet).getMessage());
            client.close();
        }else if (packet instanceof ResponseMessagePacket) {
            Bootstrap.LOGGER.severe(((ResponseMessagePacket) packet).getMessage());
        }else {
            if (packet instanceof AddPublicKeyPacket) {
                final AddPublicKeyPacket keyPacket = (AddPublicKeyPacket) packet;
                client.getKeys().put(keyPacket.getUserName(), CryptographyHelper.generatePUblicKeyFromString(keyPacket.getPublicKey()));
            }else if (packet instanceof ServerChatPacket) {
                final ServerChatPacket chatPacket = (ServerChatPacket) packet;
                Bootstrap.LOGGER.info(String.format("$purple$%s$purpleb$: $r$%s", chatPacket.getUser(), CryptographyHelper.decodeMessage(chatPacket.getMessage(), client.getKeyPair().getPrivate())));
            }
        }
    }
}
