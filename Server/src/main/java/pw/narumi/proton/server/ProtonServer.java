package pw.narumi.proton.server;

import lombok.Getter;
import lombok.Setter;
import pw.narumi.proton.network.io.PacketInputStream;
import pw.narumi.proton.network.packet.Packet;
import pw.narumi.proton.network.packet.PacketRegistry;
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

@Getter
public enum ProtonServer {
    INSTACE;

    private PacketRegistry incomingPacketRegistry;
    private PacketRegistry outgoingPacketRegistry;
    ProtonServer() {
        this.incomingPacketRegistry = new PacketRegistry(false);
        this.outgoingPacketRegistry = new PacketRegistry(true);
    }

    private Selector selector;
    private ServerSocketChannel socketChannel;

    public void initializeServer(final String ip, final int port) throws IOException {
        this.selector = Selector.open();
        this.socketChannel = ServerSocketChannel.open();
        this.socketChannel.bind(new InetSocketAddress(ip, port));
        System.out.println("Started ProtonServer on address: " + this.socketChannel.getLocalAddress());

        this.socketChannel.configureBlocking(false);
        this.socketChannel.register(this.selector, this.socketChannel.validOps());
        this.selector.select();

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

    private void accept(final Selector selector, final SelectionKey key) throws IOException {
        final SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        ClientManager.INSTANCE.addClient(new Client(channel));
    }

    private void read(final SelectionKey key) {
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

                try (final PacketInputStream inputStream = new PacketInputStream(byteBuffer.array())) {
                    final Packet packet = this.incomingPacketRegistry.createPacket(inputStream.readVarInt());

                    if (packet != null) {
                        packet.read(inputStream);
                        client.getPacketHandler().packetReceived(client, packet);
                    }
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
