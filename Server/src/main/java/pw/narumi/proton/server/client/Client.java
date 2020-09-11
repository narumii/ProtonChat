package pw.narumi.proton.server.client;

import java.nio.channels.SocketChannel;

public class Client {

    private final SocketChannel channel;
    private String username;

    public Client(final SocketChannel channel) {
        this.channel = channel;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
