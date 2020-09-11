package pw.narumi.proton.server;

import lombok.Getter;
import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketRegistry;
import pw.narumi.proton.server.client.Client;
import pw.narumi.proton.server.client.ClientManager;
import pw.narumi.proton.server.packet.incoming.ClientAddPublicKeyPacket;
import pw.narumi.proton.server.packet.incoming.ClientChatPacket;
import pw.narumi.proton.server.packet.incoming.ClientCommandPacket;
import pw.narumi.proton.server.packet.incoming.ConnectUserPacket;
import pw.narumi.proton.server.packet.outgoing.AddPublicKeyPacket;
import pw.narumi.proton.server.packet.outgoing.DisconnectPacket;
import pw.narumi.proton.server.packet.outgoing.ResponseMessagePacket;

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
                ClientAddPublicKeyPacket.class,
                ClientChatPacket.class,
                ClientCommandPacket.class,
                ConnectUserPacket.class
        );
        this.outgoingPacketRegistry.registerPackets(
                AddPublicKeyPacket.class,
                DisconnectPacket.class,
                ResponseMessagePacket.class
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

        while (this.selector.isOpen() && this.socketChannel.isOpen()) {
            this.selector.select();
            final Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                final SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }

    public void closeServer() {
        try {
            this.selector.close();
            this.socketChannel.close();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private void accept(final SelectionKey key) throws IOException {
        final SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        this.clientManager.addClient(new Client(channel));
    }

    private void read(final SelectionKey key) {
        this.clientManager.findClient((SocketChannel) key.channel()).ifPresent(client -> {
            try {
                final ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                if (client.getChannel().read(byteBuffer) == -1) {
                    this.clientManager.removeClient(client);
                    client.close();
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
                this.clientManager.removeClient(client);
                client.close();
                ex.printStackTrace();
            }
        });
    }
}
