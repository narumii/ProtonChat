package pw.narumi.proton.client.packet.outgoing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
import pw.narumi.proton.shared.packet.Packet;

@AllArgsConstructor
public class ClientResponseKeyPacket extends Packet {

    private final PublicKey publicKey;
    private final SecretKey secretKey;

    @Override
    public void read(final DataInputStream inputStream) {}

    @Override
    public void write(final DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(CryptographyHelper.serializeSecretKey(this.secretKey, this.publicKey));
    }
}
