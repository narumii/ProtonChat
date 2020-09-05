package pw.narumi.protonchat.client.command.impl.room.user;

import pw.narumi.api.room.Room;
import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.ProtonChat;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

import java.io.DataOutputStream;
import java.net.Socket;

public class QuitCommand extends Command {
    public QuitCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        if (user.getConnectedTo() == null)
            throw new CommandException("You are not connected to any room");

        try {
            final Socket socket = ProtonChat.INSTANCE.get().getSocket();
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            output.writeUTF(user.getUserName());
            output.writeLong(user.getUserId());
            output.writeUTF("quit");
        }catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
