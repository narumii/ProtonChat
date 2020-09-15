package pw.narumi.proton.client.command.impl;

import pw.narumi.proton.client.Bootstrap;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.Command;
import pw.narumi.proton.client.command.exception.CommandException;
import pw.narumi.proton.client.logger.ChatColor;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class GeneratePublicKeyCommand extends Command {

    public GeneratePublicKeyCommand(final String name, final String usage) {
        super(name, usage);
    }

    @Override
    public void invoke(final Client client, final String... args) throws CommandException {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(4096);
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();
            client.setKeyPair(keyPair);

            Bootstrap.LOGGER.info(ChatColor.RED + "Your encoded public key is: " + ChatColor.YELLOW + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        }catch (final Exception e) {
            throw new CommandException(e.getMessage());
        }
    }
}
