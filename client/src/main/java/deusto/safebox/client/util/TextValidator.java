package deusto.safebox.client.util;

import java.util.regex.Pattern;

public enum TextValidator {

    /**
     * Email regex pattern.
     *
     * @see <a href="https://emailregex.com" target="_top">Email Address Regex</a>
     */
    EMAIL("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])"),
    // PHONE_NUMBER(""),
    // ...
    ;

    private final Pattern pattern;

    TextValidator(String regexPattern) {
        pattern = Pattern.compile(regexPattern);
    }

    /**
     * Returns true if the specified string doesn't match the pattern.
     *
     * @param string the string to test.
     * @return true if the string doesn't match the pattern, otherwise false.
     */
    public boolean isNotValid(String string) {
        return !pattern.matcher(string).matches();
    }
}
