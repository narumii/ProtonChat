package pw.narumi.protonchat.client.command.impl.server;

import pw.narumi.api.user.User;
import pw.narumi.api.utils.NumberUtils;
import pw.narumi.protonchat.client.ProtonChat;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

import java.net.Socket;

public class ConnectCommand extends Command {

    public ConnectCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        if (args.length < 2)
            throw new CommandException("Usage: connect <ip> <port>");

        try {
            if (ProtonChat.INSTANCE.get().getSocket() != null) {
                System.err.println("You are already connected to the server use command \"disconnect\" to disconnect from connected server.");
                return;
            }

            final String ip = args[0];
            final int port = NumberUtils.parse(args[1], 1918);
            ProtonChat.INSTANCE.get().setSocket(new Socket(ip, port));
        }catch (final Exception e) {
            System.err.println("Can't connect to the server: " + e.getMessage());
        }
    }
}
