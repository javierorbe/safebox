package deusto.safebox.client.gui.component;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A {@link JTextArea} contained in a {@link JScrollPane}.
 */
public class ScrollTextArea extends JScrollPane {

    private final JTextArea textArea;

    public ScrollTextArea(String initialText, int textAreaRows, int textAreaColumns) {
        super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textArea = new JTextArea(initialText, textAreaRows, textAreaColumns);
        setViewportView(textArea);
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
