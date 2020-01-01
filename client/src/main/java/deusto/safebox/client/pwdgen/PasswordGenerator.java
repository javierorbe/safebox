package deusto.safebox.client.pwdgen;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("SpellCheckingInspection")
public class PasswordGenerator {

    private static final Random RANDOM = ThreadLocalRandom.current();
    private static final char[] LOWERCASE_LETTERS = "abcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final char[] AMBIGUOUS_LOWERCASE_LETTERS = "l".toCharArray();
    private static final char[] UPPERCASE_LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
    private static final char[] AMBIGUOUS_UPPERCASE_LETTERS = "IO".toCharArray();
    private static final char[] NUMBERS = "23456789".toCharArray();
    private static final char[] AMBIGUOUS_NUMBERS = "01".toCharArray();
    private static final char[] SPECIAL_CHARS = "!@#$%^&*".toCharArray();

    /**
     * Generates a password for the given options.
     *
     * @param options password generation options.
     * @return the generated password.
     */
    public static String generate(Options options) {
        String charPool = getCharPool(options);
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < options.length; i++) {
            char randChar = charPool.charAt(RANDOM.nextInt(charPool.length()));
            password.append(randChar);
        }

        return password.toString();
    }

    private static String getCharPool(Options options) {
        StringBuilder builder = new StringBuilder();

        if (options.lowerCase) {
            builder.append(LOWERCASE_LETTERS);
            if (options.ambiguous) {
                builder.append(AMBIGUOUS_LOWERCASE_LETTERS);
            }
        }

        if (options.upperCase) {
            builder.append(UPPERCASE_LETTERS);
            if (options.ambiguous) {
                builder.append(AMBIGUOUS_UPPERCASE_LETTERS);
            }
        }

        if (options.numbers) {
            builder.append(NUMBERS);
            if (options.ambiguous) {
                builder.append(AMBIGUOUS_NUMBERS);
            }
        }

        if (options.special) {
            builder.append(SPECIAL_CHARS);
        }

        return builder.toString();
    }

    public static class Options {

        private int length = 16;
        private boolean ambiguous = false;
        private boolean numbers = true;
        private boolean lowerCase = true;
        private boolean upperCase = true;
        private boolean special = false;

        public void setLength(int length) {
            this.length = length;
        }

        public boolean isAmbiguous() {
            return ambiguous;
        }

        public void setAmbiguous(boolean ambiguous) {
            this.ambiguous = ambiguous;
        }

        public boolean isNumbers() {
            return numbers;
        }

        public void setNumbers(boolean numbers) {
            this.numbers = numbers;
        }

        public boolean isSpecial() {
            return special;
        }

        public void setSpecial(boolean special) {
            this.special = special;
        }

        public boolean isLowerCase() {
            return lowerCase;
        }

        public void setLowerCase(boolean lowerCase) {
            this.lowerCase = lowerCase;
        }

        public boolean isUpperCase() {
            return upperCase;
        }

        public void setUpperCase(boolean upperCase) {
            this.upperCase = upperCase;
        }
    }

    private PasswordGenerator() {
        throw new AssertionError();
    }
}


