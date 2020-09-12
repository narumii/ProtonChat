package pw.narumi.proton.server.packet.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@AllArgsConstructor
public class ServerResponseKeyPacket extends Packet {

    private final String userName;
    private final String publicKey;

    @Override
    public void read(final DataInputStream inputStream) {}

    @Override
    public void write(final DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(this.userName);
        outputStream.writeUTF(this.publicKey);
    }
}
