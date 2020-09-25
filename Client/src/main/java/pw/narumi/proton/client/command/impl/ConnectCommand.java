package pw.narumi.proton.client.command.impl;

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

        final String ip = args[0];
        final int port = Integer.parseInt(args[1]);

        try {
            ProtonClient.INSTANCE.initializeConnection(ip, port);
        } catch (final Exception ex) {
            throw new CommandException(ex.getCause());
        }
    }
}
