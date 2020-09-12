package pw.narumi.proton.client.client;

import lombok.Getter;
import lombok.Setter;
import pw.narumi.proton.client.ProtonClient;
import pw.narumi.proton.shared.io.PacketOutputStream;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

@Getter @Setter
public class Client {

    private PacketHandler packetHandler;
    private SocketChannel channel;
    private Selector selector;
    private final String userName;

    public Client(final String userName) {
        this.userName = userName;
    }

    public void onPacketReceived(final Packet packet) {

    }

    public void sendPacket(final Packet packet) {
        final int packetID = ProtonClient.INSTANCE.getOutgoingPacketRegistry().getPacketID(packet.getClass());
        if (packetID == -1)
            return;

        try (final PacketOutputStream packetOutputStream = new PacketOutputStream()) {
            packetOutputStream.writeVarInt(packetID);
            packet.write(packetOutputStream);
            this.channel.write(packetOutputStream.asBuffer());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            this.channel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
