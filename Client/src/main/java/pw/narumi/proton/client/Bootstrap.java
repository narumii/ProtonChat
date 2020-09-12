package pw.narumi.proton.client;

import jline.console.ConsoleReader;
import jline.console.UserInterruptException;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import pw.narumi.proton.client.client.Client;
import pw.narumi.proton.client.logger.BungeeLogger;
import pw.narumi.proton.client.logger.ChatColor;
import pw.narumi.proton.client.logger.ColouredWriter;
import pw.narumi.proton.client.logger.LoggingOutputStream;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Bootstrap {

    public static Logger LOGGER = Logger.getLogger("ProtonChat");
    private static String prefix = "Gimme your name: ";

    public static void main(final String... args) throws IOException {
        System.setProperty("library.jansi.version", "ProtonChat");
        AnsiConsole.systemInstall();

        final ConsoleReader consoleReader = new ConsoleReader();
        consoleReader.setHandleUserInterrupt(true);
        consoleReader.setExpandEvents(true);

        LOGGER = new BungeeLogger("ProtonChat", consoleReader);
        System.setErr(new PrintStream(new LoggingOutputStream(LOGGER, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(LOGGER, Level.INFO), true));

        try {
            String line;
            while ((line = consoleReader.readLine(prefix)) != null) {
                if (ProtonClient.INSTANCE.getClient() == null) {
                    ProtonClient.INSTANCE.setClient(new Client((line.contains(" ") ? line.split(" ")[0] : line)));
                    prefix = "> ";
                    continue;
                }

                ProtonClient.INSTANCE.getCommandManager().handleCommand(line);
            }
        }catch (final UserInterruptException e) {
            System.exit(0);
        }
    }

    public static void setPrefix(final String prefix) {
        Bootstrap.prefix = prefix;
    }


}
