package pw.narumi.proton.client.packet.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Getter @AllArgsConstructor
public class ClientChatPacket extends Packet {

    private String toUser;
    private String message;

    @Override
    public void read(final DataInputStream inputStream) throws IOException {
        this.toUser = inputStream.readUTF();
        this.message = inputStream.readUTF();
    }

    @Override
    public void write(final DataOutputStream outputStream) {}
}
