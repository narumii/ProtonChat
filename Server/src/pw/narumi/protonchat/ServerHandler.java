package pw.narumi.protonchat;

import pw.narumi.Bootstrap;
import pw.narumi.protonchat.io.ProtonInputStream;
import pw.narumi.protonchat.io.ProtonOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler implements Handler {

    private final Map<String, Socket> connectedUsers = new HashMap<>();
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
            if (!validateUser(socket)) {
                socket.close();
            }
        }
    }

    @Override
    public boolean validateUser(final Socket socket) throws Exception {
        final ProtonInputStream inputStream = new ProtonInputStream(socket.getInputStream());
        final ProtonOutputStream outputStream = new ProtonOutputStream(socket.getOutputStream());

        outputStream.writeString("LOGIN");

        final String user = Bootstrap.getProtonChat().decrypt(inputStream.readString());
        final String password = Bootstrap.getProtonChat().decrypt(inputStream.readString());

        //FIXME: Dodac baze danych i jebane sprawdzanie oraz aesa h3h
        if (!user.equals("test1") && !password.equals("chujnia1")) {
            outputStream.writeBoolean(false);
            socket.close();
            return false;
        }

        outputStream.writeBoolean(true);
        connectedUsers.put(user, socket);
        addMessageHandler(user, socket);
        return true;
    }

    @Override
    public void addMessageHandler(final String nick, final Socket socket) throws Exception {
        new Thread(() -> {
            try {
                final ProtonInputStream inputStream = new ProtonInputStream(socket.getInputStream());
                while (socket.isConnected() && inputStream.available() > 0) {
                    final String message = inputStream.readString();
                    sendMessage(nick, message);
                }
            }catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void sendMessage(final String nick, final String message) throws Exception {
        for (Map.Entry<String, Socket> connectedUser : connectedUsers.entrySet()) {
            if (!connectedUser.getKey().equals(nick)) {
                final ProtonOutputStream outputStream = new ProtonOutputStream(connectedUser.getValue().getOutputStream());
                outputStream.writeString(nick);
                outputStream.writeString(message);
            }
        }
    }
}
