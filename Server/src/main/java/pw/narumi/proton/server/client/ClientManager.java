package pw.narumi.proton.server.client;

import pw.narumi.proton.shared.packet.Packet;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientManager {

    private final List<Client> clients = new CopyOnWriteArrayList<>();

    public void addClient(final Client client) {
        this.clients.add(client);
    }

    public void removeClient(final Client client) {
        this.clients.remove(client);
    }

    public void sendPacket(final Packet packet) {
        this.clients.forEach(client -> client.sendPacket(packet));
    }

    public Optional<Client> findClient(final SocketChannel channel) {
        return this.clients.stream()
                .filter(client -> client.getChannel().equals(channel))
                .findFirst();
    }
}
