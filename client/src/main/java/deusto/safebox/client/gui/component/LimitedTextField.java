package deusto.safebox.client.gui.component;

import deusto.safebox.client.gui.LimitedDocument;
import javax.swing.JTextField;

/**
 * A {@link JTextField} with limited text length.
 */
public class LimitedTextField extends JTextField {

    /**
     * Creates a {@link LimitedTextField} with the specified limit.
     *
     * @param limit text length limit.
     * @param initialText initial text content.
     * @param beep if true, a beep sound is emitted when the trying to surpass the limit.
     */
    public LimitedTextField(int limit, String initialText, boolean beep) {
        super(new LimitedDocument(limit, beep), initialText, 0);
    }

    /**
     * Creates a {@link LimitedTextField} with the specified limit.
     *
     * @param limit text length limit.
     * @param beep if true, a beep sound is emitted when the trying to surpass the limit.
     */
    public LimitedTextField(int limit, boolean beep) {
        this(limit, null, beep);
    }
}
