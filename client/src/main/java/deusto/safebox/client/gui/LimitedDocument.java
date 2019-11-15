package deusto.safebox.client.gui;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/** Document that has a limited length. */
@SuppressWarnings("serial")
public class LimitedDocument extends PlainDocument {

    private final int limit;
    private final boolean beep;

    /**
     * A document with a length limit.
     *
     * @param limit the limit.
     * @param beep if true, a beep sound is emitted when the trying to surpass the limit.
     */
    public LimitedDocument(int limit, boolean beep) {
        this.limit = limit;
        this.beep = beep;
    }

    public LimitedDocument(int limit) {
        this(limit, false);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str != null) {
            if ((getLength() + str.length()) <= limit) {
                super.insertString(offs, str, a);
            } else if (beep) {
                beep();
            }
        }
    }

    private static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }
}
