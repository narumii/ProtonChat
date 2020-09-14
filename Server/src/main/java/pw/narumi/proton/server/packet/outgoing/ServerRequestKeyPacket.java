package pw.narumi.proton.server.packet.outgoing;

import lombok.AllArgsConstructor;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

@AllArgsConstructor
public class ServerRequestKeyPacket extends Packet {

    @Override
    public void read(final DataInputStream inputStream) {}

    @Override
    public void write(final DataOutputStream outputStream) {}
}
