package pw.narumi.proton.client.packet.incoming;

import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerChatPacket extends Packet {

    private final String user;
    private final String message;

    public ServerChatPacket(final String user, final String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public void read(final DataInputStream inputStream) {}

    @Override
    public void write(final DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(user);
        outputStream.writeUTF(message);
    }
}
