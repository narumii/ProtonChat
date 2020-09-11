package pw.narumi.proton.network.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PacketOutputStream extends DataOutputStream {

    public PacketOutputStream() {
        super(new ByteArrayOutputStream());
    }

    public void writeVarInt(int value) throws IOException {
        do {
            byte temp = (byte)(value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            writeByte(temp);
        } while (value != 0);
    }

    public ByteBuffer toBuffer() {
        return ByteBuffer.wrap(((ByteArrayOutputStream) out).toByteArray());
    }
}