package pw.narumi.protonchat.client.command.impl.room.user;

import pw.narumi.api.room.Room;
import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.ProtonChat;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

import java.io.DataOutputStream;
import java.net.Socket;

public class CreateCommand extends Command {

    public CreateCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        if (user.getConnectedTo() != null)
            throw new CommandException("You are already connected to room");

        if (args.length != 2)
            throw new CommandException("Usage: create <name> <password>");

        try {
            final String name = args[0];
            final String password = args[1];
            final Room room = new Room(name, user);
            room.setRoomPassword(password.getBytes());

            final Socket socket = ProtonChat.INSTANCE.get().getSocket();
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            output.writeUTF("@create");
            output.writeLong(user.getUserId());
            output.writeUTF(name);
            output.writeUTF(password);
        }catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
