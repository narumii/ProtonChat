package pw.narumi.proton.server.client;

import lombok.Data;
import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.server.packet.outgoing.ServerRequestHandshakePacket;
import pw.narumi.proton.server.packet.outgoing.ServerRequestKeyPacket;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Data
public class Client {

    private final SocketChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);
    private final PacketHandler packetHandler = new ClientPacketHandler(this);
    private String username;
    private boolean logged;

    public void requestLogin() {
        sendPacket(new ServerRequestHandshakePacket());
        sendPacket(new ServerRequestKeyPacket());
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
