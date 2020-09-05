package pw.narumi.protonchat.client.command.impl.hash;

import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.ProtonChat;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class SetDeocdeKeyCommand extends Command {

    public SetDeocdeKeyCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        try {
            final byte[] key = Base64.getDecoder().decode(args[1].getBytes());
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            user.getDecodeKeys().put(args[0], privateKey);
            System.out.println("Properly added " + args[0].toUpperCase() + " message decode key.");
        }catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
