package pw.narumi.proton.client.packet.outgoing;

import lombok.AllArgsConstructor;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@AllArgsConstructor
public class ClientChatPacket extends Packet {

    private final String message;

    @Override
    public void read(final DataInputStream inputStream) {}

    @Override
    public void write(final DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(this.message);
    }
}
