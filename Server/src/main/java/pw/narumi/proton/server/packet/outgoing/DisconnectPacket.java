package pw.narumi.proton.server.packet.outgoing;

import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.io.PacketOutputStream;
import pw.narumi.proton.shared.packet.Packet;

import java.io.IOException;

public class DisconnectPacket implements Packet {

    private final String message;

    public DisconnectPacket(final String message) {
        this.message = message;
    }

    @Override
    public void read(final PacketInputStream inputStream) {}

    @Override
    public void write(final PacketOutputStream outputStream) throws IOException {
        outputStream.writeUTF(message);
    }
}
