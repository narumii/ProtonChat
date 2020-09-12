package pw.narumi.proton.shared.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {

    public abstract void read(final DataInputStream dataInputStream) throws IOException;

    public abstract void write(final DataOutputStream dataOutputStream) throws IOException;

    public final void handle(final PacketHandler packetHandler) {
        packetHandler.packetReceived(this);
    }
}
