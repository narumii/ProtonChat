package pw.narumi.proton.client.command.impl;

import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.Command;
import pw.narumi.proton.client.command.exception.CommandException;

public class ExitCommand extends Command {

    public ExitCommand(final String name, final String usage) {
        super(name, usage);
    }

    @Override
    public void invoke(final Client client, final String... args) throws CommandException {
        System.exit(1);
    }
}
