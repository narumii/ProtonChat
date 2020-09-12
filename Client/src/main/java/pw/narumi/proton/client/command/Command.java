package pw.narumi.proton.client.command;

import lombok.Data;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.exception.CommandException;

@Data
public abstract class Command {

    private final String name;
    private final String usage;

    public abstract void invoke(final Client client, final String... args) throws CommandException;
}
