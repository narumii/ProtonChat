package pw.narumi.proton.client;

import lombok.Getter;
import lombok.Setter;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.command.CommandManager;
import pw.narumi.proton.client.command.impl.key.GeneratePublicKeyCommand;
import pw.narumi.proton.client.command.impl.server.ConnectCommand;
import pw.narumi.proton.client.packet.incoming.AddPublicKeyPacket;
import pw.narumi.proton.client.packet.incoming.DisconnectPacket;
import pw.narumi.proton.client.packet.incoming.ResponseMessagePacket;
import pw.narumi.proton.client.packet.incoming.ServerChatPacket;
import pw.narumi.proton.client.packet.outgoing.ClientAddPublicKeyPacket;
import pw.narumi.proton.client.packet.outgoing.ClientChatPacket;
import pw.narumi.proton.client.packet.outgoing.ClientCommandPacket;
import pw.narumi.proton.client.packet.outgoing.ConnectUserPacket;
import pw.narumi.proton.shared.logger.Logger;
import pw.narumi.proton.shared.packet.Packet;
import pw.narumi.proton.shared.packet.PacketRegistry;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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
                new ConnectCommand("connect", "connect <ip> <port>"),
                new GeneratePublicKeyCommand("generateKey", null)
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

    @Setter
    private Client client;

    public void initializeConnection(final String ip, final int port, final Runnable connectAction) throws IOException {
        final Selector selector = Selector.open();
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(ip, port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        Bootstrap.LOGGER.info("$green$Connected to server: $r$" + socketChannel.getRemoteAddress() + "\n");
        Bootstrap.setPrefix(Logger.PURPLE + this.client.getUserName() + Logger.PURPLE_BRIGHT + ": " + Logger.RESET);
        connectAction.run();
        this.client.setChannel(socketChannel);

        new Thread(() -> {
            try {
                while (this.client.getChannel().isOpen()) {
                    selector.select();
                    final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        final SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable())
                            read();
                    }
                }
            } catch (final IOException ex) {
                System.out.println();
                ex.printStackTrace();
            }

            throw new ThreadDeath();
        }).start();
    }

    private void read() {
        try {
            final ByteBuffer buffer = this.client.getBuffer();
            buffer.clear();
            if (this.client.getChannel().read(buffer) == -1) {
                this.client.close();
                Bootstrap.setPrefix("$purple$> $r$");
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
            System.out.println();
            ex.printStackTrace();
            Bootstrap.setPrefix("$purple$> $r$");
            this.client.close();
        }
    }
 }
