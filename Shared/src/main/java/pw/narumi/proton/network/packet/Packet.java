package pw.narumi.proton.network.packet;

import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;

import java.io.IOException;

public interface Packet {

    void read(final PacketInputStream inputStream) throws IOException;

    void write(final PacketOutputStream outputStream) throws IOException;
}
