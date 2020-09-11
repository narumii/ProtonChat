package pw.narumi.proton.server.packet.outgoing;

import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.io.PacketOutputStream;
import pw.narumi.proton.shared.packet.Packet;

import java.io.IOException;

public class AddPublicKeyPacket extends Packet {

    private final String userName;
    private final String publicKey;

    public AddPublicKeyPacket(final String userName, final String publicKey) {
        this.userName = userName;
        this.publicKey = publicKey;
    }

    @Override
    public void read(final PacketInputStream inputStream) {}

    @Override
    public void write(final PacketOutputStream outputStream) throws IOException {
        outputStream.writeUTF(userName);
        outputStream.writeUTF(publicKey);
    }
}
