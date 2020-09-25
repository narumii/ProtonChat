package pw.narumi.proton.client.packet.incoming;

import java.security.PublicKey;
import lombok.Getter;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Getter
public class ServerRequestKeyPacket extends Packet {

    private PublicKey publicKey;

    @Override
    public void read(final DataInputStream inputStream) throws IOException {
        this.publicKey = CryptographyHelper.deserializePublicKey(inputStream.readUTF());
    }

    @Override
    public void write(final DataOutputStream outputStream) {}
}
