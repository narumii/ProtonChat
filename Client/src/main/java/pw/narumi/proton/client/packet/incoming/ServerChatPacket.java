package pw.narumi.proton.client.packet.incoming;

import lombok.Getter;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Getter
public class ServerChatPacket extends Packet {

    private String user;
    private String message;

    @Override
    public void read(final DataInputStream inputStream) throws IOException {
        this.user = inputStream.readUTF();
        this.message = inputStream.readUTF();
    }

    @Override
    public void write(final DataOutputStream outputStream) {}
}
