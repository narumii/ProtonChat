package pw.narumi.proton.client;

import pw.narumi.proton.client.client.Client;

import java.util.Scanner;

public class Bootstrap {

    private static String prefix = "Gimme your name: ";

    public static void main(final String... args) {
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prefix);
            final String string = scanner.nextLine();
            if (ProtonClient.INSTANCE.getClient() == null) {
                ProtonClient.INSTANCE.setClient(new Client((string.contains(" ") ? string.split(" ")[0] : string)));
                prefix = "> ";
                continue;
            }

            ProtonClient.INSTANCE.getCommandManager().handleCommand(string);
        }
    }

    public static void setPrefix(final String prefix) {
        Bootstrap.prefix = prefix;
    }
}
