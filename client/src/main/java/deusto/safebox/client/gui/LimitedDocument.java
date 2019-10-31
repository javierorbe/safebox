package deusto.safebox.client.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/** Document that has a limited length. */
@SuppressWarnings("serial")
public class LimitedDocument extends PlainDocument {

    private final int limit;

    public LimitedDocument(int limit) {
        this.limit = limit;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str != null && (getLength() + str.length()) <= limit) {
            super.insertString(offs, str, a);
        }
    }
}
