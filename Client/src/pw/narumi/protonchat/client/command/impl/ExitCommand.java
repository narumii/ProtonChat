package pw.narumi.protonchat.client.command.impl;

import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

public class ExitCommand extends Command {
    public ExitCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        System.exit(-1);
    }
}
