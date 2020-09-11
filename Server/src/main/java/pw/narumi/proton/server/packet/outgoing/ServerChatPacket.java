package pw.narumi.proton.server.packet.outgoing;

import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.io.PacketOutputStream;
import pw.narumi.proton.shared.packet.Packet;

import java.io.IOException;

public class ServerChatPacket extends Packet {

    private final String user;
    private final String message;

    public ServerChatPacket(final String user, final String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public void read(final PacketInputStream inputStream) {}

    @Override
    public void write(final PacketOutputStream outputStream) throws IOException {
        outputStream.writeUTF(user);
        outputStream.writeUTF(message);
    }
}
