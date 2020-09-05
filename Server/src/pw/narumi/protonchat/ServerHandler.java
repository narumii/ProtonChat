package pw.narumi.protonchat;

import pw.narumi.api.user.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler implements Handler {

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
}
