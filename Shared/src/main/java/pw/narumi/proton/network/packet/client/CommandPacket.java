package pw.narumi.proton.network.packet.client;

import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;
import pw.narumi.proton.network.packet.Packet;

import java.io.IOException;

public class CommandPacket implements Packet {

    private long userId;
    private String command;

    public CommandPacket(final long userId, final String command) {
        this.userId = userId;
        this.command = command;
    }

    @Override
    public void read(final PacketInputStream inputStream) throws IOException {
        this.userId = inputStream.readLong();
        this.command = inputStream.readUTF();
    }

    @Override
    public void write(final PacketOutputStream outputStream) throws IOException {
        outputStream.writeLong(this.userId);
        outputStream.writeUTF(this.command);
    }
}
