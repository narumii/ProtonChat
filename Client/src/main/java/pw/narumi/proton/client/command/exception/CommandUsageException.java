package pw.narumi.proton.client.command.exception;

public class CommandUsageException extends CommandException {

    public CommandUsageException(final String message) {
        super(message);
    }

    public CommandUsageException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommandUsageException(final Throwable cause) {
        super(cause);
    }

    public CommandUsageException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
