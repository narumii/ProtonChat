package pw.narumi.proton.server.packet.incoming;

import lombok.Getter;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Getter
public class ClientCommandPacket extends Packet {

    private String command;

    @Override
    public void read(final DataInputStream inputStream) throws IOException{
        this.command = inputStream.readUTF();
    }

    @Override
    public void write(final DataOutputStream outputStream) {}
}
