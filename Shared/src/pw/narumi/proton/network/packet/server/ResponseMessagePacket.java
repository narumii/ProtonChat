package pw.narumi.proton.network.packet.server;

import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;
import pw.narumi.proton.network.packet.Packet;

public class ResponseMessagePacket extends Packet {

    private final String message;

    public ResponseMessagePacket(final String message) {
        this.message = message;
    }

    @Override
    public void read(final PacketInputStream inputStream) throws Exception {}

    @Override
    public void write(final PacketOutputStream outputStream) throws Exception {
        outputStream.writeUTF(this.message);
    }
}
