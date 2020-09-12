package pw.narumi.proton.client;

import lombok.Getter;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.CommandManager;
import pw.narumi.proton.client.command.impl.server.ConnectCommand;
import pw.narumi.proton.client.packet.incoming.*;
import pw.narumi.proton.client.packet.outgoing.*;
import pw.narumi.proton.shared.io.PacketInputStream;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Getter
public enum ProtonClient {
    INSTANCE;

    private final CommandManager commandManager;
    private final PacketRegistry incomingPacketRegistry;
    private final PacketRegistry outgoingPacketRegistry;

    ProtonClient() {
        this.commandManager = new CommandManager();
        this.incomingPacketRegistry = new PacketRegistry(false);
        this.outgoingPacketRegistry = new PacketRegistry(true);

        this.commandManager.registerCommands(
                new ConnectCommand("connect", "connect <ip> <port>")
        );

        this.outgoingPacketRegistry.registerPackets(
                ClientAddPublicKeyPacket.class,
                ClientChatPacket.class,
                ClientCommandPacket.class,
                ConnectUserPacket.class
        );
        this.incomingPacketRegistry.registerPackets(
                AddPublicKeyPacket.class,
                DisconnectPacket.class,
                ResponseMessagePacket.class,
                ServerChatPacket.class
        );
    }

    private Client client;

    public void setClient(final Client client) {
        this.client = client;
        System.out.println("You logged as: " + client.getUserName());
    }

    private SocketChannel socketChannel;
    private Selector selector;

    public void initializeConnection(final String ip, final int port) throws IOException {
        this.selector = Selector.open();
        this.socketChannel = SocketChannel.open();
        this.socketChannel.connect(new InetSocketAddress(ip, port));
        this.socketChannel.configureBlocking(false);
        this.socketChannel.register(this.selector, SelectionKey.OP_READ);
        client.setChannel(this.socketChannel);
        client.setSelector(this.selector);
        System.out.println("Connected to server: " + this.socketChannel.getRemoteAddress());
        Bootstrap.setPrefix(client.getUserName() + ": ");

        new Thread(() -> {
            try {
                while (this.selector.isOpen() && this.socketChannel.isOpen()) {
                    this.selector.select();
                    final Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        final SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            read();
                        }
                    }
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
            throw new ThreadDeath();
        }).start();
    }


    private void read() {
        try {
            final ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
            if (this.client.getChannel().read(byteBuffer) == -1) {
                this.client.close();
                return;
            }

            try (final PacketInputStream inputStream = new PacketInputStream(byteBuffer.array())) {
                final Packet packet = this.incomingPacketRegistry.createPacket(inputStream.readVarInt());
                if (packet != null) {
                    packet.read(inputStream);
                    packet.handle(client.getPacketHandler());
                }
            }
        } catch (final IOException ex) {
            this.client.close();
            ex.printStackTrace();
        }
    }
}
