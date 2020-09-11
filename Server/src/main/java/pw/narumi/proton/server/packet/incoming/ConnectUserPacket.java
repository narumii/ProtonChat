package pw.narumi.proton.server.packet.incoming;


import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.io.PacketOutputStream;
import pw.narumi.proton.network.packet.Packet;

import java.io.IOException;

public class ConnectUserPacket implements Packet {

    private final String userName;
    private final long id;

    public ConnectUserPacket(final String userName, final long id) {
        this.userName = userName;
        this.id = id;
    }

    @Override
    public void read(final PacketInputStream inputStream) throws IOException {}

    @Override
    public void write(final PacketOutputStream outputStream) throws IOException {
        outputStream.writeUTF(userName);
        outputStream.writeLong(id);
    }
}
