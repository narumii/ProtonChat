package pw.narumi.proton.server.packet.outgoing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
import pw.narumi.proton.shared.packet.Packet;

@AllArgsConstructor
public class ServerRequestKeyPacket extends Packet {

    @Override
    public void read(final DataInputStream inputStream) {}

    @Override
    public void write(final DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(CryptographyHelper.serializePublicKey(ProtonServer.INSTANCE.getKeyPair().getPublic()));
    }
}
