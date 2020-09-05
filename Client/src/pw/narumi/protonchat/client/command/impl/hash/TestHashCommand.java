package pw.narumi.protonchat.client.command.impl.hash;

import pw.narumi.api.user.User;
import pw.narumi.api.utils.Cryptography;
import pw.narumi.protonchat.client.ProtonChat;
import pw.narumi.protonchat.client.command.Command;
import pw.narumi.protonchat.client.command.CommandException;

public class TestHashCommand extends Command {

    public TestHashCommand(final String alias) {
        super(alias);
    }

    @Override
    public void execute(final User user, final String... args) throws CommandException {
        if (ProtonChat.INSTANCE.get().getUser().getKeyPair() == null)
            throw new CommandException("You must first generate key using command: generatekey");

        if (args.length < 1)
            throw new CommandException("Usage: testhash <to encode>");

        final String toEncode = args[0];
        final String encoded = Cryptography.encode(toEncode, user.getKeyPair().getPublic());
        final String decoded = Cryptography.decode(encoded, user.getKeyPair().getPrivate());

        System.out.println("Original: " + toEncode);
        System.out.println("Encoded: " + encoded);
        System.out.println("Decoded: " + decoded);
    }
}
