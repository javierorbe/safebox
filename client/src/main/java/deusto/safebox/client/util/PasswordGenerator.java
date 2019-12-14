package deusto.safebox.client.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PasswordGenerator {

    private static final char[] UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final char[] LOWER = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static final char[] NUMBERS = "0123456789".toCharArray();

    private static final char[] SYMBOLS = "^*-_.,;:¿?/&%$!=".toCharArray();

    private static final Random RANDOM = ThreadLocalRandom.current();

    public static String generatePassword(int passLength, boolean vUpper, boolean vLower,
                                          boolean vNumber, boolean vSymbols) {
        StringBuilder list = new StringBuilder();
        StringBuilder password = new StringBuilder();

        if (vUpper) {
            list.append(UPPER);
        }
        if (vLower) {
            list.append(LOWER);
        }
        if (vNumber) {
            list.append(NUMBERS);
        }
        if (vSymbols) {
            list.append(SYMBOLS);
        }

        for (int i = 0; i < passLength; i++) {
            password.append(list.charAt(RANDOM.nextInt(list.length())));
        }

        return password.toString();
    }
}
