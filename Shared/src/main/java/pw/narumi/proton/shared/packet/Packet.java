package pw.narumi.proton.shared.packet;

import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.io.PacketOutputStream;

import java.io.IOException;

public abstract class Packet {

    public abstract void read(final PacketInputStream inputStream) throws IOException;

    public abstract void write(final PacketOutputStream outputStream) throws IOException;

    public final void handle(final PacketHandler packetHandler) {
        packetHandler.packetReceived(this);
    }
}
