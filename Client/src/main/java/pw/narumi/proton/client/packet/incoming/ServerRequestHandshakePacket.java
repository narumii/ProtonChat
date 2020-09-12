package pw.narumi.proton.client.packet.incoming;

import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ServerRequestHandshakePacket extends Packet {

    @Override
    public void read(final DataInputStream inputStream) {}

    @Override
    public void write(final DataOutputStream outputStream) {}
}
