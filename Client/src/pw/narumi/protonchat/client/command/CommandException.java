package pw.narumi.protonchat.client.command;

public class CommandException extends Exception {

    public CommandException(final String message) {
        super(message);
    }

    public CommandException(final Throwable cause) {
        super(cause);
    }
}
