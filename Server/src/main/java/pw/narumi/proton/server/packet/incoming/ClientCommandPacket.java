package pw.narumi.proton.server.packet.incoming;

import lombok.Getter;
import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.io.PacketOutputStream;
import pw.narumi.proton.shared.packet.Packet;

import java.io.IOException;

@Getter
public class ClientCommandPacket implements Packet {

    private long userId;
    private String command;

    @Override
    public void read(final PacketInputStream inputStream) throws IOException {
        this.userId = inputStream.readLong();
        this.command = inputStream.readUTF();
    }

    @Override
    public void write(final PacketOutputStream outputStream) {}
}
