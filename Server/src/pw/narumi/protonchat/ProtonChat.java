package pw.narumi.protonchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ProtonChat {

    private final String ip;
    private final int port;
    private Handler handler;

    public ProtonChat(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }

    public ProtonChat startDB() {
        return this;
    }

    public void setHandler(final Handler handler) throws Exception {
        this.handler = handler;
        handler.setServerSocket(startServer());
    }

    public ServerSocket startServer() throws IOException {
        final ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(ip, port));
        return serverSocket;
    }

    public Handler getHandler() {
        return this.handler;
    }
}
