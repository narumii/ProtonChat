package pw.narumi.proton.client.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.exception.CommandException;

@AllArgsConstructor @Getter
public abstract class Command {

    private final String name;
    private final String usage;

    public abstract void invoke(final Client client, final String... args) throws CommandException;
}
