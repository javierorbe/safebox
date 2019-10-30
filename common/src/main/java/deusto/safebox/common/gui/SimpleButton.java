package deusto.safebox.common.gui;

import javax.swing.JButton;

public class SimpleButton extends JButton {

    public SimpleButton(String text) {
        super(text);
        setRequestFocusEnabled(false);
        setFocusPainted(false);
    }
}
