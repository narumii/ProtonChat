package pw.narumi.proton.client.packet.outgoing;

import lombok.Getter;
import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.io.PacketOutputStream;
import pw.narumi.proton.shared.packet.Packet;

import java.io.IOException;

@Getter
public class ClientAddPublicKeyPacket extends Packet {

    private String publicKey;

    @Override
    public void read(final PacketInputStream inputStream) throws IOException {
        this.publicKey = inputStream.readUTF();
    }

    @Override
    public void write(final PacketOutputStream outputStream) {}
}
