package pw.narumi.proton.client.logger;

import jline.console.ConsoleReader;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class BungeeLogger extends Logger {

    private final LogDispatcher dispatcher = new LogDispatcher(this);

    public BungeeLogger(String loggerName, ConsoleReader reader) {
        super(loggerName, null);
        setLevel(Level.ALL);

        try {
            ColouredWriter consoleHandler = new ColouredWriter(reader);
            consoleHandler.setFormatter(new ConciseFormatter(true));
            addHandler(consoleHandler);
        } catch (Exception ex) {
            System.err.println("Could not register logger!");
            ex.printStackTrace();
        }

        dispatcher.start();
    }

    @Override
    public void log(LogRecord record) {
        dispatcher.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
    }
}
