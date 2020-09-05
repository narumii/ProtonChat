package pw.narumi.protonchat;

import java.net.ServerSocket;
import java.net.Socket;

public interface Handler {

    void setServerSocket(final ServerSocket serverSocket) throws Exception;

    void acceptConnection() throws Exception;

    boolean validateUser(final Socket socket) throws Exception;

    void addMessageHandler(final String nick, final Socket socket) throws Exception;

    void sendMessage(final String nick, final String message) throws Exception;
}
