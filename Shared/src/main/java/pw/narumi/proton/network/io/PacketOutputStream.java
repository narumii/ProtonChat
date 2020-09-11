package pw.narumi.proton.network.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PacketOutputStream extends DataOutputStream {

    public PacketOutputStream() {
        super(new ByteArrayOutputStream());
    }

    public void writeVarInt(int value) throws IOException {
        do {
            byte temp = (byte) (value & 127);
            value >>>= 7;
            if (value != 0)
                temp |= 128;

            writeByte(temp);
        } while (value != 0);
    }

    public ByteBuffer asBuffer() {
        return ByteBuffer.wrap(((ByteArrayOutputStream) out).toByteArray());
    }
}