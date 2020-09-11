package pw.narumi.proton.network.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PacketInputStream extends DataInputStream {

    public PacketInputStream(final byte[] buf) {
        super(new ByteArrayInputStream(buf));
    }

    public int readVarInt() throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = readByte();
            result |= ((read & 127) << (7 * numRead));
            if (numRead++ > 5)
                throw new RuntimeException("VarInt is too big!");
        } while ((read & 128) != 0);

        return result;
    }
}
