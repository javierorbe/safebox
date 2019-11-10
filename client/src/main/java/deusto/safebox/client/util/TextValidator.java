package deusto.safebox.client.util;

import java.util.regex.Pattern;

public class TextValidator {

    /**
     * Email regex pattern.
     *
     * @see <a href="https://emailregex.com" target="_top">Email Address Regex</a>
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    /**
     * Returns {@code true} if the given string is an email address.
     *
     * @param string the string to test.
     * @return true if the string is an email address, otherwise false.
     */
    public static boolean isValidEmail(String string) {
        return EMAIL_PATTERN.matcher(string).matches();
    }

    private TextValidator() {
        throw new AssertionError();
    }
}
