package deusto.safebox.client.gui.component;

import javax.swing.JPasswordField;

/**
 * A {@link JPasswordField} where the password can be shown or hidden.
 */
public class ShowPasswordField extends JPasswordField {

    private static final char HIDDEN_CHAR = '●';

    /**
     * Creates a {@link ShowPasswordField} with the specified column count.
     *
     * @param columns column count.
     * @param show if true, the password is initially shown;
     *             otherwise, the password is initially hidden.
     */
    public ShowPasswordField(int columns, boolean show) {
        super(columns);

        if (show) {
            showPassword();
        }
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
