package pw.narumi.protonchat;

import pw.narumi.Bootstrap;
import pw.narumi.api.user.User;
import pw.narumi.protonchat.command.Command;
import pw.narumi.protonchat.command.impl.room.admin.DeleteCommand;
import pw.narumi.protonchat.command.impl.room.admin.KickCommand;
import pw.narumi.protonchat.command.impl.room.admin.NameCommand;
import pw.narumi.protonchat.command.impl.room.admin.PasswordCommand;
import pw.narumi.protonchat.command.impl.room.user.CreateCommand;
import pw.narumi.protonchat.command.impl.room.user.JoinCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ServerHandler implements Handler {

    private final Map<String, Command> commands = new HashMap<>(){{
        commands.put("delete", new DeleteCommand());
        commands.put("kick", new KickCommand());
        commands.put("name", new NameCommand());
        commands.put("password", new PasswordCommand());
        commands.put("create", new CreateCommand());
        commands.put("join", new JoinCommand());
    }};

    private final Map<User, Socket> connectedUsers = new HashMap<>();
    private ServerSocket serverSocket;

    @Override
    public void setServerSocket(final ServerSocket serverSocket) throws Exception {
        this.serverSocket = serverSocket;
    }

    @Override
    public void acceptConnection() throws Exception {
        while (serverSocket.isBound()) {
            System.out.println("connected");
            final Socket socket = serverSocket.accept();
            addUser(socket);
        }
    }

    private void addUser(final Socket socket) throws Exception {
        final DataInputStream input = new DataInputStream(socket.getInputStream());
        final String name = input.readUTF();
        final long id = input.readLong();

        final User user = new User(name, id);
        connectedUsers.put(user, socket);
        addMessageHandler(user, socket);
    }

    @Override
    public void addMessageHandler(final User user, final Socket socket) throws Exception {
        new Thread(() -> {
            try {
                final DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                while (socket.isConnected() && inputStream.available() > 0) {
                    final String message = inputStream.readUTF();

                    if (isCommand(message))
                        handleCommand(user, message);
                    else
                        sendMessage(user, message);
                }
            }catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void sendMessage(final User user, final String message) throws Exception {
        for (Map.Entry<User, Socket> connectedUser : connectedUsers.entrySet()) {
            if (connectedUser.getKey().getUserId() != user.getUserId()) {
                final DataOutputStream outputStream = new DataOutputStream(connectedUser.getValue().getOutputStream());
                outputStream.writeUTF(user.getUserName());
                outputStream.writeUTF(message);
            }
        }
    }

    private void handleCommand(final User user, final String string) throws IOException {
        final Socket socket = this.connectedUsers.get(user);
        if (socket.getInetAddress().getHostAddress().equals(user.getAddress())) {
            final DataInputStream input = new DataInputStream(socket.getInputStream());
            final long userId = input.readLong();
            if (userId == user.getUserId()) {
                try {
                    commands.get(string.replace("@", "")).execute(user, socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isCommand(final String string) {
        return string.startsWith("@") && commands.get(string.replace("@", "")) != null;
    }

    public Map<User, Socket> getConnectedUsers() {
        return this.connectedUsers;
    }
}
