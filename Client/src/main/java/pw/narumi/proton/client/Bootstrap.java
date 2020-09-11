package pw.narumi.proton.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public final class Bootstrap {

    public static void main(final String... args) throws IOException {
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 1918));
        socketChannel.configureBlocking(false);

        //TODO: ADD MESSAGE/COMMAND/CONNECT HANDLERS
    }
}
