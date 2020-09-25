package pw.narumi.proton.server.packet.incoming;

import javax.crypto.SecretKey;
import lombok.Getter;
import pw.narumi.proton.server.ProtonServer;
import pw.narumi.proton.shared.cryptography.CryptographyHelper;
import pw.narumi.proton.shared.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Getter
public class ClientResponseKeyPacket extends Packet {

    private SecretKey secretKey;

    @Override
    public void read(final DataInputStream inputStream) throws IOException {
        this.secretKey = CryptographyHelper.deserializeSecretKey(inputStream.readUTF(), ProtonServer.INSTANCE.getKeyPair().getPrivate());
    }

    @Override
    public void write(final DataOutputStream outputStream) {}
}
