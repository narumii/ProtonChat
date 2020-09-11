package pw.narumi.proton.server.client;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public enum ClientManager {

    INSTANCE;

    private final List<Client> clients = new CopyOnWriteArrayList<>();

    public void addClient(final Client client) {
        clients.add(client);
    }

    public void removeClient(final Client client) {
        clients.remove(client);
    }

    public Optional<Client> findClient(final SocketChannel channel) {
        return clients.stream()
                .filter(client -> client.getChannel().equals(channel))
                .findFirst();
    }
}
