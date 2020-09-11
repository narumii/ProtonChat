package pw.narumi.proton.server;

import pw.narumi.proton.server.client.Client;
import pw.narumi.proton.server.client.ClientManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public final class Bootstrap {

    public static void main(final String... args) throws IOException {
        final Selector selector = Selector.open();
        final ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress("127.0.0.1", 1918));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, socketChannel.validOps());
        selector.select();

        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            final SelectionKey key = iterator.next();
            iterator.remove();
            if (!key.isValid())
                continue;

            if (key.isAcceptable()) {
                accept(selector, key);
                continue;
            }

            read(key);
        }
    }

    private static void accept(final Selector selector, final SelectionKey key) throws IOException {
        final SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        ClientManager.INSTANCE.addClient(new Client(channel));
    }

    private static void read(final SelectionKey key) {
        ClientManager.INSTANCE.findClient((SocketChannel) key.channel()).ifPresent(client -> {
            try {
                final SocketChannel channel = client.getChannel();
                final ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                if (channel.read(byteBuffer) == -1) {
                    ClientManager.INSTANCE.removeClient(client);
                    channel.close();
                    key.cancel();
                    return;
                }

                // TODO: 11.09.2020 - handlowanie pakietuf
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
