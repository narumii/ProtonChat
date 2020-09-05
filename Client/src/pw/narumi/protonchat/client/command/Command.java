package pw.narumi.protonchat.client.command;

import pw.narumi.api.user.User;

public abstract class Command {

    private final String alias;

    public Command(final String alias) {
        this.alias = alias;
    }

    public abstract void execute(final User user, final String... args) throws CommandException;

    public String getAlias() {
        return this.alias;
    }
}
