package pw.narumi.proton.server.client;

import lombok.Getter;
import lombok.Setter;
import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Getter @Setter
public class Client {

    private final SocketChannel channel;
    private final PacketHandler packetHandler;
    private String username;
    private boolean logged;

    public Client(final SocketChannel channel) {
        this.channel = channel;
        this.packetHandler = new ClientPacketHandler(this);
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

        try (
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)
        ) {
            dataOutputStream.writeByte(packetID);
            packet.write(dataOutputStream);
            this.channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
