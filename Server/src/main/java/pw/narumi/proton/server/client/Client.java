package pw.narumi.proton.server.client;

import lombok.Getter;
import lombok.Setter;
import pw.narumi.proton.shared.io.PacketOutputStream;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.server.packet.PacketHandler;

import java.io.IOException;
import java.nio.channels.SocketChannel;

@Getter @Setter
public class Client {

    private final SocketChannel channel;
    private String username;
    private long userId;
    private PacketHandler packetHandler;

    public Client(final SocketChannel channel) {
        this.channel = channel;
    }

    public void close() {
        try {
            this.channel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendPacket(final Packet packet) {
        final int packetID = ProtonServer.INSTANCE.getOutgoingPacketRegistry().getPacketID(packet.getClass());
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
}
