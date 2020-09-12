package pw.narumi.proton.client.command.impl.key;

import pw.narumi.proton.client.Bootstrap;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.Command;
import pw.narumi.proton.client.command.exception.CommandException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class GeneratePublicKeyCommand extends Command {

    public GeneratePublicKeyCommand(final String name, final String usage) {
        super(name, usage);
    }

    @Override
    public void invoke(final Client client, final String... args) throws CommandException {
        try {
            final long start = System.currentTimeMillis();
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(4096);
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();
            client.setKeyPair(keyPair);

            Bootstrap.LOGGER.info("$red$Your encode message code: $r$" + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            Bootstrap.LOGGER.info("$red$Generated keys in: $r$" + (TimeUnit.MILLISECONDS.toSeconds( (System.currentTimeMillis() - start) )) + "s");
        }catch (final Exception e) {
            throw new CommandException(e.getMessage());
        }
    }
}
