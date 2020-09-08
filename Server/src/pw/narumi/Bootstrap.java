package pw.narumi;

import pw.narumi.protonchat.ProtonChat;
import pw.narumi.protonchat.ServerHandler;

import java.net.Socket;

public class Bootstrap {

    private static ProtonChat protonChat;

    public static void main(final String... args) throws Exception {
        final ProtonChat protonChat = new ProtonChat("127.0.0.1", 1918);

        protonChat
                .startDB()
                .setHandler(new ServerHandler());

        Bootstrap.protonChat = protonChat;

        while (true){}
    }

    public static ProtonChat getProtonChat() {
        return protonChat;
    }
}
