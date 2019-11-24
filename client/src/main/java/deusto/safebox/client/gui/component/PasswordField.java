package deusto.safebox.client.gui.component;

import javax.swing.JPasswordField;
import javax.swing.text.Document;

/**
 * A {@link JPasswordField} where the password can be shown or hidden.
 */
public class PasswordField extends JPasswordField {

    private static final char HIDDEN_CHAR = '●';

    /**
     * Creates a {@link PasswordField} with the specified column count.
     *
     * @param document the text document of the field.
     * @param initialPassword the initial password.
     * @param show if true, the password is initially shown;
     *             otherwise, the password is initially hidden.
     */
    private PasswordField(Document document, String initialPassword, boolean show) {
        super(document, initialPassword, 0);

        if (show) {
            showPassword();
        }
    }

    /**
     * Creates a {@link PasswordField} with the specified column count.
     *
     * @param limit password size limit.
     * @param initialPassword the initial password.
     * @param show if true, the password is initially shown;
     *             otherwise, the password is initially hidden.
     */
    public PasswordField(int limit, String initialPassword, boolean show) {
        this(new LimitedDocument(limit, true), initialPassword, show);
    }

    public PasswordField(int limit, boolean show) {
        this(limit, null, show);
    }

    public PasswordField(String initialPassword, boolean show) {
        this(null, initialPassword, show);
    }

    public void showPassword() {
        setEchoChar((char) 0);
        putClientProperty("JPasswordField.cutCopyAllowed", true);
    }

    public void hidePassword() {
        setEchoChar(HIDDEN_CHAR);
        putClientProperty("JPasswordField.cutCopyAllowed", false);
    }
}
