package pw.narumi.protonchat.client.command.impl.server;

import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.ProtonChat;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

public class DisconnectCommand extends Command {

    public DisconnectCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        try {
            if (ProtonChat.INSTANCE.get().getSocket() == null) {
                System.err.println("You are not connected to any server.");
                return;
            }

            ProtonChat.INSTANCE.get().getSocket().close();
            ProtonChat.INSTANCE.get().setSocket(null);
        }catch (final Exception e) {
            System.err.println("Can't disconnect from the server: " + e.getMessage());
        }
    }
}