package pw.narumi.proton.client.command;

import pw.narumi.proton.client.Bootstrap;
import pw.narumi.proton.client.ProtonClient;
import pw.narumi.proton.client.command.exception.CommandException;
import pw.narumi.proton.client.command.exception.CommandUsageException;
import pw.narumi.proton.client.packet.outgoing.ClientChatPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();

    public void handleCommand(final String string) {
        if (string.isEmpty())
            return;

        if (ProtonClient.INSTANCE.getClient().getChannel() == null || string.startsWith("/")) {
            boolean hasPrefix = string.startsWith("/");
            final String[] args = hasPrefix ? string.substring(1).split(" ") : string.split(" ");
            final String commandAlias = args[0];
            final Optional<Command> command = getCommand(commandAlias);

            if (command.isPresent()) {
                try {
                    final String[] commandArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
                    command.get().invoke(ProtonClient.INSTANCE.getClient(), commandArgs);
                } catch (final CommandException exception) {
                    if (exception instanceof CommandUsageException)
                        Bootstrap.LOGGER.info("$red$Usage: $r$" + exception.getMessage());
                    else
                        Bootstrap.LOGGER.info(String.format("$red$Error processing command $yellow$\"%s\"$r$: %s", commandAlias.toUpperCase(), exception.getMessage()));
                }
                return;
            }
            Bootstrap.LOGGER.info(String.format("$red$Command $yellow$\"%s\" $red$not found, use command $yellow$\"help\" $red$to check command list.", commandAlias.toUpperCase()));
        }else if (ProtonClient.INSTANCE.getClient().getChannel() != null && !string.startsWith("/")) {
            ProtonClient.INSTANCE.getClient().sendPacket(new ClientChatPacket("123", "123"));
            ProtonClient.INSTANCE.getClient().getKeys().forEach((userName, key) -> ProtonClient.INSTANCE.getClient().sendPacket(new ClientChatPacket(userName, /*Encode*/string)));
        }
    }

    public void registerCommands(final Command... commands) {
        for (final Command command : commands) {
            this.commands.put(command.getName().toLowerCase(), command);
        }
    }

    private Optional<Command> getCommand(final String alias) {
        return Optional.ofNullable(commands.get(alias.toLowerCase()));
    }
}
