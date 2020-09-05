package pw.narumi.api.utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class StringUtils {

    public static String getRandomString(final int length) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append("QWERTYUIOPASDFGHJKLZXCVBNM0123456789".charAt(ThreadLocalRandom.current().nextInt("QWERTYUIOPASDFGHJKLZXCVBNM0123456789".length())));
        }
        return stringBuilder.toString();
    }

    public static String getRandomUTF(int length) {
        final char[] chars = new char[length];

        for (int i = 0; i < length; i++) {
            chars[i] = (char) (ThreadLocalRandom.current().nextInt(Short.MAX_VALUE) + 1337);
        }

        return new String(chars).intern();
    }
    public static void centerPrint(final String string, final int size) {
        final int left = (size - string.length()) / 2;
        final int right = (size - left) - string.length();
        final StringBuilder stringBuilder = new StringBuilder();

        IntStream.range(0, left).forEach(i -> stringBuilder.append(" "));
        stringBuilder.append(string);
        IntStream.range(0, right).forEach(i -> stringBuilder.append(" "));

        System.out.println(stringBuilder.toString());
    }
}
