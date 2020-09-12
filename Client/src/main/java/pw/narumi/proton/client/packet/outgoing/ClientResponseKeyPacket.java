package pw.narumi.proton.client.packet.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@AllArgsConstructor @Getter
public class ClientResponseKeyPacket extends Packet {

    private final String userName;
    private final String publicKey;

    @Override
    public void read(final DataInputStream inputStream) throws IOException {

    }

    @Override
    public void write(final DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(userName);
        outputStream.writeUTF(publicKey);
    }
}
