package pw.narumi.proton.server.packet.incoming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Getter
public class ClientResponseKeyPacket extends Packet {

    private String userName;
    private String publicKey;

    @Override
    public void read(final DataInputStream inputStream) throws IOException {
        this.userName = inputStream.readUTF();
        this.publicKey = inputStream.readUTF();
    }

    @Override
    public void write(final DataOutputStream outputStream) { }
}