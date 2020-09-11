package pw.narumi.proton.server.packet.incoming;

import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;
import pw.narumi.proton.network.packet.Packet;

import java.io.IOException;

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
    public void write(final PacketOutputStream outputStream) throws IOException {

    }
}
