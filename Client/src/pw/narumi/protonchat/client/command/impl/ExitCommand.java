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
        System.out.println("\r");
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            System.out.println(" ");
        }
        System.out.println("\n");
        System.exit(-1);
    }
}
