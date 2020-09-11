package pw.narumi.proton.network.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }
}
