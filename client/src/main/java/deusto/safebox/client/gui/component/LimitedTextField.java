package deusto.safebox.client.gui.component;

import deusto.safebox.client.gui.LimitedDocument;
import javax.swing.JTextField;

public class LimitedTextField extends JTextField {

    public LimitedTextField(int limit) {
        this(null, 0, limit);
    }

    public LimitedTextField(String text, int limit) {
        this(text, 0, limit);
    }

    public LimitedTextField(int columns, int limit) {
        this(null, columns, limit);
    }

    public LimitedTextField(String text, int columns, int limit) {
        super(text, columns);
        setDocument(new LimitedDocument(limit));
    }
}
