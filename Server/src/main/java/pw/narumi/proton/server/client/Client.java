package pw.narumi.proton.server.client;

import javax.crypto.SecretKey;
import lombok.Data;
import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.server.packet.outgoing.ServerRequestHandshakePacket;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
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
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final PacketHandler packetHandler = new ClientPacketHandler(this);
    private String userName;
    private SecretKey secretKey;
    private boolean logged;

    public void requestLogin() {
        sendPacket(new ServerRequestHandshakePacket());
    }

    public void close() {
        try {
            this.channel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendPacket(final Packet packet) {
        if (this.channel == null || !this.channel.isOpen())
            return;

        final int packetID = ProtonServer.INSTANCE.getOutgoingPacketRegistry().getPacketID(packet.getClass());
        if (packetID == -1)
            return;

        try (
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)
        ) {
            dataOutputStream.writeByte(packetID);
            packet.write(dataOutputStream);
            final byte[] data = byteArrayOutputStream.toByteArray();
            this.channel.write(ByteBuffer.wrap((this.secretKey != null ? CryptographyHelper.encodeData(data, this.secretKey) : data)));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
