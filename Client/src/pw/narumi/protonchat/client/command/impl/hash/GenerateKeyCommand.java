package pw.narumi.protonchat.client.command.impl.hash;

import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.sql.Time;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class GenerateKeyCommand extends Command {

    public GenerateKeyCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        try {
            System.out.println("Generating keys...");
            final long start = System.currentTimeMillis();
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(4096);
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();

            System.out.println("Your encode message code: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            System.out.println("Your decode message code: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

            user.setKeyPair(keyPair);
            System.out.println("Generated keys in: " + (TimeUnit.MILLISECONDS.toSeconds( (System.currentTimeMillis() - start) )) + "s");
        }catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
