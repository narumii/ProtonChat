package pw.narumi.proton.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class Bootstrap {

    public static void main(final String... args) throws IOException {
        final ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress("127.0.0.1", 1918));
        socketChannel.configureBlocking(false);

        //TODO: ADD MESSAGE/COMMAND/CONNECT HANDLERS
    }
}