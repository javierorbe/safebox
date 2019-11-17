package deusto.safebox.client.gui.component;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RightAlignedLabel extends JLabel {

    public RightAlignedLabel(String text) {
        super(text);
        setHorizontalAlignment(SwingConstants.RIGHT);
    }
}
