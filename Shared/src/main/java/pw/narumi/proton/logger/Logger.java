package pw.narumi.proton.logger;

public class Logger {

    public static final String PURPLE = "\033[0;35m";
    public static final String PURPLE_BRIGHT = "\033[0;95m";
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;91m";
    public static final String GREEN = "\033[0;92m";
    public static final String YELLOW = "\u001B[33m";

    private static String fixColors(final String string) {
        return string
                .replace("$purple$", PURPLE)
                .replace("$purpleb$", PURPLE_BRIGHT)
                .replace("$red$", RED)
                .replace("$r$", RESET)
                .replace("$green$", GREEN)
                .replace("$yellow$", YELLOW);

    }

    public void print(final String string) {
        System.out.print(fixColors(string));
    }

    public void info(final String string) {
        System.out.println(fixColors(string));
    }

    public void success(final String string) {
        System.out.println(GREEN + string + RESET);
    }

    public void warning(final String string) {
        System.out.println(YELLOW + string + RESET);
    }

    public void severe(final String string) {
        System.out.println(RED + string + RESET);
    }
}
