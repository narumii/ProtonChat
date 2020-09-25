package pw.narumi.proton.client.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import javax.crypto.SecretKey;
import lombok.Data;
import pw.narumi.proton.client.ProtonClient;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

@Data
public class Client {

    private final String userName;
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);
    private final PacketHandler packetHandler = new ClientPacketHandler(this);
    private SocketChannel channel;
    private SecretKey secretKey;

    public void sendPacket(final Packet packet) {
        if (this.channel == null || !this.channel.isOpen())
            return;

        final int packetID = ProtonClient.INSTANCE.getOutgoingPacketRegistry().getPacketID(packet.getClass());
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

    public void close() {
        if (this.channel == null || !this.channel.isOpen())
            return;

        try {
            this.channel.close();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
