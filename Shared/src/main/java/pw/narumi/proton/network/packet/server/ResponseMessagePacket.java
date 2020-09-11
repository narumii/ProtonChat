package pw.narumi.proton.network.packet.server;

import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;
import pw.narumi.proton.network.packet.Packet;

import java.io.IOException;

public class ResponseMessagePacket implements Packet {

    private final String message;

    public ResponseMessagePacket(final String message) {
        this.message = message;
    }

    @Override
    public void read(final PacketInputStream inputStream) {

    }

    @Override
    public void write(final PacketOutputStream outputStream) throws IOException {
        outputStream.writeUTF(this.message);
    }
}
