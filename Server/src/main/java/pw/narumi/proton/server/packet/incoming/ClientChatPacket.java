package pw.narumi.proton.server.packet.incoming;

import lombok.Getter;
import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.io.PacketOutputStream;
import pw.narumi.proton.shared.packet.Packet;

import java.io.IOException;

@Getter
public class ClientChatPacket implements Packet {

    private String userName;
    private long userID;
    private String message;

    @Override
    public void read(final PacketInputStream inputStream) throws IOException {
        this.userName = inputStream.readUTF();
        this.userID = inputStream.readLong();
        this.message = inputStream.readUTF();
    }

    @Override
    public void write(final PacketOutputStream outputStream) {}
}
