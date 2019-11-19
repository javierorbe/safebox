package deusto.safebox.common.gui;

import javax.swing.JButton;

public class SimpleButton extends JButton {

    /**
     * Creates a button with the specified text.
     *
     * @param text the text of the button.
     */
    public SimpleButton(String text) {
        super(text);
        setRequestFocusEnabled(false);
        setFocusPainted(false);
    }

    /**
     * Creates a button with the specified text and action.
     *
     * @param text the text of the button.
     * @param action the action to be called when the button is pressed.
     */
    public SimpleButton(String text, Runnable action) {
        this(text);
        addAction(action);
    }

    public void addAction(Runnable runnable) {
        addActionListener(e -> runnable.run());
    }
}
