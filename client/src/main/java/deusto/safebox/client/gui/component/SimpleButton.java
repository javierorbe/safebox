package deusto.safebox.client.gui.component;

import javax.swing.JButton;

public class SimpleButton extends JButton {

    public SimpleButton(String text) {
        super(text);
        setRequestFocusEnabled(false);
    }
}
