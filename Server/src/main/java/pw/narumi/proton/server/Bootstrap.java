package pw.narumi.proton.server;

import java.io.IOException;
import java.util.Scanner;

public final class Bootstrap {

    public static void main(final String... args) throws IOException {
        int port = 1918;
        try {
            if (args.length != 1) {
                final Scanner scanner = new Scanner(System.in);
                System.out.print("Server port: ");
                port = scanner.nextInt();
            } else {
                port = Integer.parseInt(args[0]);
            }
        } catch (final Exception ex) {
            System.err.println("Invalid server port, using the default one (1918).");
        }

        ProtonServer.INSTANCE.initializeServer("0.0.0.0", port);
        Runtime.getRuntime().addShutdownHook(new Thread(ProtonServer.INSTANCE::closeServer));
    }
}
