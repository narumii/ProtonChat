package pw.narumi.proton.client.packet.incoming;

import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AddPublicKeyPacket extends Packet {

    private final String userName;
    private final String publicKey;

    public AddPublicKeyPacket(final String userName, final String publicKey) {
        this.userName = userName;
        this.publicKey = publicKey;
    }

    @Override
    public void read(final DataInputStream inputStream) {}

    @Override
    public void write(final DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(userName);
        outputStream.writeUTF(publicKey);
    }
}
