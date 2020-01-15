package deusto.safebox.client.locale;

public enum Message {

    ABOUT("About"),
    ACCOUNT("Account"),
    ACCOUNT_CREATED("Account successfully created."),
    ACCOUNT_HOLDER("Account Holder"),
    APPEARANCE("Appearance"),
    APPLY("Apply"),
    BANK_NAME("Bank Name"),
    CANCEL("Cancel"),
    CATEGORIES("Categories"),
    CARD_HOLDER("Card Holder"),
    CONFIRM_PASSWORD("Confirm Password"),
    CONFIRM_PASSWORD_MATCH("The confirm password doesn't match the password."),
    CONTENT("Content"),
    CREATED("Created"),
    CREATE_ACCOUNT("Create Account"),
    CLOSE("Close"),
    DATA_SUCCESSFULLY_SAVED("Data was successfully saved."),
    EMAIL("Email"),
    EMAIL_ALREADY_USED("Email already in use."),
    EXPIRATION("Expiration"),
    FOLDERS("Folders"),
    FULL_NAME("Full Name"),
    HELP("Help"),
    INVALID_EMAIL_ADDRESS("Invalid email address."),
    INVALID_LOGIN_DETAILS("Invalid login details."),
    MINIMIZE("Minimize"),
    NEW_FOLDER("New Folder"),
    NEW_ITEM("New Item"),
    NUMBER("Number"),
    PASSWORD("Password"),
    PASSWORD_GENERATOR("Password Generator"),
    REMEMBER_EMAIL("Remember Email"),
    SAVE_DATA("Save Data"),
    SEARCH("Search"),
    SETTINGS("Settings"),
    SIGN_IN("Sign In"),
    SIGN_OUT("Sign Out"),
    STATION_NAME("Station Name"),
    STATION_PASSWORD("Station Password"),
    LANGUAGE("Language"),
    LAST_MODIFIED("Last Modified"),
    LOCATION("Location"),
    THEME("Theme"),
    TITLE("Title"),
    TYPE("Type"),
    UNKNOWN_ERROR("Unknown Error"),
    USERNAME("Username"),
    WEBSITE("Website"),
    ;

    private final String defaultMessage;

    Message(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    private String getTranslatedMessage() {
        return LocaleManager.getTranslation(this).orElse(defaultMessage);
    }

    public String get() {
        return getTranslatedMessage();
    }

    @Override
    public String toString() {
        return get();
    }
}
