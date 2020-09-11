package pw.narumi.proton.shared.packet;

import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.io.PacketOutputStream;

import java.io.IOException;

public interface Packet {

    void read(final PacketInputStream inputStream) throws IOException;

    void write(final PacketOutputStream outputStream) throws IOException;
}
