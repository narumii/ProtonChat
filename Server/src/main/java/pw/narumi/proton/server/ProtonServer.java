package pw.narumi.proton.server;

import lombok.Getter;
import pw.narumi.proton.server.client.Client;
import pw.narumi.proton.server.client.ClientManager;
import pw.narumi.proton.server.packet.incoming.*;
import pw.narumi.proton.server.packet.outgoing.*;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketRegistry;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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

    INSTANCE;

    private final ClientManager clientManager;
    private final PacketRegistry incomingPacketRegistry;
    private final PacketRegistry outgoingPacketRegistry;

    ProtonServer() {
        this.clientManager = new ClientManager();
        this.incomingPacketRegistry = new PacketRegistry(false);
        this.outgoingPacketRegistry = new PacketRegistry(true);
        this.incomingPacketRegistry.registerPackets(
                ClientChatPacket.class,
                ClientCommandPacket.class,
                ClientHandshakePacket.class,
                ClientRequestKeyPacket.class,
                ClientResponseKeyPacket.class
        );
        this.outgoingPacketRegistry.registerPackets(
                ServerChatPacket.class,
                ServerDisconnectPacket.class,
                ServerRequestHandshakePacket.class,
                ServerRequestKeyPacket.class,
                ServerResponseKeyPacket.class,
                ServerResponseMessagePacket.class
        );
    }

    private Selector selector;
    private ServerSocketChannel socketChannel;

    public void initializeServer(final String ip, final int port) throws IOException {
        this.selector = Selector.open();
        this.socketChannel = ServerSocketChannel.open();
        this.socketChannel.bind(new InetSocketAddress(ip, port));
        this.socketChannel.configureBlocking(false);
        this.socketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("Started ProtonServer on address: " + this.socketChannel.getLocalAddress());

        new Thread(() -> {
            try {
                while (this.socketChannel.isOpen()) {
                    this.selector.select();
                    final Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        final SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable())
                            accept(key);
                        else if (key.isReadable())
                            read(key);
                    }
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }

            throw new ThreadDeath();
        }).start();
    }

    public void closeServer() {
        try {
            this.socketChannel.close();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private void accept(final SelectionKey key) throws IOException {
        final SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);

        final Client client = new Client(channel);
        client.requestLogin();
        this.clientManager.addClient(client);
    }

    private void read(final SelectionKey key) {
        this.clientManager.findClient((SocketChannel) key.channel()).ifPresent(client -> {
            try {
                final ByteBuffer buffer = client.getBuffer();
                buffer.clear();
                if (client.getChannel().read(buffer) == -1) {
                    this.clientManager.removeClient(client);
                    client.close();
                    return;
                }

                try (final DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(buffer.array()))) {
                    final Packet packet = this.incomingPacketRegistry.createPacket(inputStream.readByte());
                    if (packet != null) {
                        packet.read(inputStream);
                        packet.handle(client.getPacketHandler());
                    }
                }
            } catch (final IOException ex) {
                this.clientManager.removeClient(client);
                client.close();
                ex.printStackTrace();
            }
        });
    }
}
