package pw.narumi.proton.client.command;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pw.narumi.proton.client.ProtonClient;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.exception.CommandException;
import pw.narumi.proton.client.command.exception.CommandUsageException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandManager {

    private final Map<String, Command> commandMap = new HashMap<>();

    public void handleCommand(final String string) {
        if (string.isEmpty())
            return;

        final String[] args = string.split(" ");
        final String commandAlias = args[0];
        final Optional<Command> command = getCommand(commandAlias);

        if (command.isPresent()) {
            try {
                final String[] commandArgs = new String[args.length - 1];
                System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
                command.get().invoke(ProtonClient.INSTANCE.getClient(), commandArgs);
            }catch (final CommandException exception) {
                if (exception instanceof CommandUsageException)
                    System.err.println("Usage: " + exception.getMessage());
                else
                    System.err.println(String.format("Error processing command \"%s\": %s", commandAlias.toUpperCase(), exception.getMessage()));
            }
            return;
        }
        System.err.println(String.format("Command \"%s\" not found, use command \"help\" to check command list.", commandAlias.toUpperCase()));
    }

    public void registerCommands(final Command... commands) {
        for (final Command command : commands) {
            this.commandMap.put(command.getName(), command);
        }
    }

    private Optional<Command> getCommand(final String alias) {
        return Optional.ofNullable(commandMap.get(alias));
    }
}
