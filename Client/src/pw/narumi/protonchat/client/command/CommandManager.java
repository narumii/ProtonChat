package pw.narumi.protonchat.client.command;

import pw.narumi.api.lazy.Lazy;
import pw.narumi.api.user.User;
import pw.narumi.api.utils.NumberUtils;
import pw.narumi.protonchat.client.ProtonChat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandManager {

    public static String PREFIX = "Gimme your name: ";
    private final Map<String, Command> commandMap = new HashMap<>();
    private final Lazy<User> user = new Lazy<>() {
        @Override
        public User load() {
            return ProtonChat.INSTANCE.get().getUser();
        }
    };

    public void handleCommand(final String string) {
        if (string.isEmpty() || string.isBlank()) {
            System.out.println("Command can't be blank empty!");
            return;
        }

        if (ProtonChat.INSTANCE.get().getUser() == null) {
            final String userName = string.contains(" ") ? string.split(" ")[0].trim() : string;
            ProtonChat.INSTANCE.get().setUser(new User(userName, NumberUtils.randomLong(Long.MAX_VALUE - (Long.MAX_VALUE / 2), Long.MAX_VALUE)));
            CommandManager.PREFIX = "> ";
        }else {
            if (user.get().getConnectedTo() != null) {
                if (string.startsWith("@"))
                    callCommand(string);
                else
                    sendMessage(string);
            }else {
                callCommand(string);
            }
        }
    }

    private void sendMessage(final String string) {}

    private void callCommand(final String string) {
        final String[] split = string.trim().split(" ");
        final String commandName = split[0];
        final Optional<Command> command = getCommand(commandName);

        if (command.isPresent()) {
            try {
                final String[] args = new String[split.length - 1];
                System.arraycopy(split, 1, args, 0, split.length - 1);
                command.get().execute(user.get(), args);
            } catch (final CommandException exception) {
                System.out.println(String.format("Error occured while processing %s command: %s", commandName.toUpperCase(), exception.getMessage()));
            }
            return;
        }
        System.out.println(String.format("Command %s not found, type help for more informations!", commandName.toUpperCase()));
    }

    public void addCommand(final Command... commands) {
        for (final Command command : commands) {
            this.commandMap.put(command.getAlias(), command);
        }
    }

    private Optional<Command> getCommand(final String string) {
        return Optional.ofNullable(this.commandMap.get(string.toLowerCase()));
    }

    public Map<String, Command> getCommandMap() {
        return this.commandMap;
    }
}