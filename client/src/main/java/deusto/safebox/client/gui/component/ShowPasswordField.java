package deusto.safebox.client.gui.component;

import javax.swing.JPasswordField;

public class ShowPasswordField extends JPasswordField {

    private static final char HIDDEN_CHAR = '●';

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
