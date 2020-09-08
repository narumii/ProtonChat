package pw.narumi.protonchat.client.command.impl.room.admin;

import pw.narumi.api.room.Room;
import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.ProtonChat;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

import java.io.DataOutputStream;
import java.net.Socket;

public class KickCommand extends Command {
    public KickCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        if (args.length != 1)
            throw new CommandException("Usage: kick <nickname>");

        try {
            final Socket socket = ProtonChat.INSTANCE.get().getSocket();
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            output.writeUTF("@kick");
            output.writeLong(user.getUserId());
            output.writeUTF(args[0]);
        }catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
