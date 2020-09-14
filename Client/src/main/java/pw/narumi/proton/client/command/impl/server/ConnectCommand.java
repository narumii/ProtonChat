package pw.narumi.proton.client.command.impl.server;

import pw.narumi.proton.client.ProtonClient;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.Command;
import pw.narumi.proton.client.command.exception.CommandException;
import pw.narumi.proton.client.command.exception.CommandUsageException;

public class ConnectCommand extends Command {

    public ConnectCommand(final String name, final String usage) {
        super(name, usage);
    }

    @Override
    public void invoke(final Client client, final String... args) throws CommandException {
        if (args.length != 2)
            throw new CommandUsageException(this.getUsage());

        if (client.getKeyPair() == null)
            throw new CommandException("At first you must generate your publicKey using command \"generateKey\"");

        final String ip = args[0];
        final int port = Integer.parseInt(args[1]);

        try {
            ProtonClient.INSTANCE.initializeConnection(ip, port);
            //ProtonClient.INSTANCE.initializeConnection(ip, port, () -> {
            //    client.sendPacket(new ClientHandshakePacket(client.getUserName()));
            //    client.sendPacket(new ClientAddPublicKeyPacket(Base64.getEncoder().encodeToString(client.getKeyPair().getPublic().getEncoded())));
            //});
        } catch (final Exception e) {
            throw new CommandException(e.getMessage());
        }
    }
}
