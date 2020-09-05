package pw.narumi.api.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class NumberUtils {

    public static int secureRandomInt(final int min, final int max) throws NoSuchAlgorithmException {
        if (min >= max) {
            return 1;
        }

        return SecureRandom.getInstanceStrong().nextInt(max) + max - min;
    }

    public static int randomInt(final int min, final int max) {
        if (min >= max) {
            return 1;
        }

        return ThreadLocalRandom.current().nextInt(max) + max - min;
    }

    public static double calculatePercentage(final double obtained, final double total) {
        return obtained * 100 / total;
    }

    public static int parse(final String toParse, final int errorReturn) {
        try {
            return Integer.parseInt(toParse);
        }catch (final Exception e) {
            return errorReturn;
        }
    }

    public static long randomLong(long min, long max) {
        try {
            return (SecureRandom.getInstanceStrong().nextLong() % (max - min)) + min;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
