package pw.narumi.proton.network.packet;

import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;

public abstract class Packet {

    public abstract void read(final PacketInputStream inputStream) throws Exception;

    public abstract void write(final PacketOutputStream outputStream) throws Exception;
}
