package pw.narumi.proton.server.packet.outgoing;

import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;
import pw.narumi.proton.network.packet.Packet;

import java.io.IOException;

public class AddPublicKeyPacket implements Packet {

    private String userName;
    private String publicKey;

    public AddPublicKeyPacket(final String userName, final String publicKey) {
        this.userName = userName;
        this.publicKey = publicKey;
    }

    @Override
    public void read(final PacketInputStream inputStream) throws IOException {
    }

    @Override
    public void write(final PacketOutputStream outputStream) throws IOException {
        outputStream.writeUTF(userName);
        outputStream.writeUTF(publicKey);
    }
}
