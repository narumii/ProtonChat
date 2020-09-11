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
        } catch (final Exception e) {
            System.err.println("Invalid port, setting default port value. (1918)");
        }

        ProtonServer.INSTANCE.initializeServer("127.0.0.1", port);
    }
}
