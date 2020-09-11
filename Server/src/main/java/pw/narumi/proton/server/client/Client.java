package pw.narumi.proton.server.client;

import lombok.Getter;
import lombok.Setter;
import pw.narumi.proton.server.packet.PacketHandler;

import java.nio.channels.SocketChannel;

@Getter @Setter
public class Client {

    private final SocketChannel channel;
    private String username;
    private long userId;
    private PacketHandler packetHandler;

    public Client(final SocketChannel channel) {
        this.channel = channel;
    }
}
