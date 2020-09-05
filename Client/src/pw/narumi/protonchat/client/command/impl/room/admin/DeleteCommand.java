package pw.narumi.protonchat.client.command.impl.room.admin;

import pw.narumi.api.room.Room;
import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.ProtonChat;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

import java.io.DataOutputStream;
import java.net.Socket;

public class DeleteCommand extends Command {

    public DeleteCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        final Room room = user.getConnectedTo();
        if (user.getUserId() != room.getRoomAdmin().getUserId())
            throw new CommandException("You are not room admin.");

        try {
            final Socket socket = ProtonChat.INSTANCE.get().getSocket();
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            output.writeLong(user.getUserId());
            output.writeUTF("delete");
        }catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
