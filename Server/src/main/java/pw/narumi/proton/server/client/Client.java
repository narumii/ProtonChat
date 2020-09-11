package pw.narumi.proton.server.client;

import pw.narumi.proton.server.packet.PacketHandler;

import java.nio.channels.SocketChannel;

public class Client {

    private final SocketChannel channel;
    private String username;
    private PacketHandler packetHandler;

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

    public PacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public void setPacketHandler(final PacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }
}
