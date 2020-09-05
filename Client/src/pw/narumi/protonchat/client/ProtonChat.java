package pw.narumi.protonchat.client;

import jline.console.ConsoleReader;
import pw.narumi.api.lazy.Lazy;
import pw.narumi.api.user.User;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandManager;

import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProtonChat {

    public static final Lazy<ProtonChat> INSTANCE = new Lazy<>() {
        @Override
        public ProtonChat load() {
            return new ProtonChat();
        }
    };


    public final Lazy<CommandManager> commandManager = new Lazy<>() {
        @Override
        public CommandManager load() {
            return new CommandManager();
        }
    };

    private User user;
    private Socket socket;

    public void registerCommand(final Command... commands) {
        this.commandManager.get().addCommand(commands);
    }

    public ProtonChat handleConsole() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                final Scanner scanner = new Scanner(System.in);

                while (!executorService.isShutdown()) {
                    System.out.print(CommandManager.PREFIX);
                    final String string = scanner.nextLine();
                    this.commandManager.get().handleCommand(string);
                }
            }catch (final  Exception exception) {exception.printStackTrace();}
        });

        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(final Socket socket) {
        this.socket = socket;
    }
}
