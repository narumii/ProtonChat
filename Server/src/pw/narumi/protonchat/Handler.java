package pw.narumi.protonchat;

import pw.narumi.api.user.User;

import java.net.ServerSocket;
import java.net.Socket;

public interface Handler {

    void setServerSocket(final ServerSocket serverSocket) throws Exception;

    void acceptConnection() throws Exception;

    void addMessageHandler(final User user, final Socket socket) throws Exception;

    void sendMessage(final User user, final String message) throws Exception;
}
