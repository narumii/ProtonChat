package pw.narumi.proton.server.packet.incoming;

import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;
import pw.narumi.proton.network.packet.Packet;

import java.io.IOException;

public class ClientCommandPacket implements Packet {

    private long userId;
    private String command;

    @Override
    public void read(final PacketInputStream inputStream) throws IOException {
        this.userId = inputStream.readLong();
        this.command = inputStream.readUTF();
    }

    @Override
    public void write(final PacketOutputStream outputStream) throws IOException {}
}
