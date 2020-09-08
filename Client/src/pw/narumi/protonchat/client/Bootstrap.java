package pw.narumi.protonchat.client;

import pw.narumi.protonchat.client.command.impl.ExitCommand;
import pw.narumi.protonchat.client.command.impl.hash.GenerateKeyCommand;
import pw.narumi.protonchat.client.command.impl.hash.SetDeocdeKeyCommand;
import pw.narumi.protonchat.client.command.impl.hash.TestHashCommand;
import pw.narumi.protonchat.client.command.impl.server.ConnectCommand;
import pw.narumi.protonchat.client.command.impl.server.DisconnectCommand;

public class Bootstrap {

    public static void main(final String... args) {
        final ProtonChat protonChat = ProtonChat.INSTANCE.get();
        protonChat.registerCommand(
                new GenerateKeyCommand("generatekey"),
                new SetDeocdeKeyCommand("setdecodekey"),
                new ExitCommand("e"),
                new TestHashCommand("testhash"),
                new ConnectCommand("connect"),
                new DisconnectCommand("disconnect")
        );
        protonChat.handleConsole();
    }
}
