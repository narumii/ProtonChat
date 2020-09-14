package pw.narumi.proton.client.client;

import lombok.Data;
import pw.narumi.proton.client.ProtonClient;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Client {

    private final Map<String, PublicKey> keys = new ConcurrentHashMap<>();
    private final String userName;
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);
    private final PacketHandler packetHandler = new ClientPacketHandler(this);
    private SocketChannel channel;
    private KeyPair keyPair;

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
            this.channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        if (this.channel == null || !this.channel.isOpen())
            return;

        try {
            this.channel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
