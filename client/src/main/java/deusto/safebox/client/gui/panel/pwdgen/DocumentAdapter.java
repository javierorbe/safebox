package deusto.safebox.client.gui.panel.pwdgen;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** A document listener that runs a callback when its content is modified. */
class DocumentAdapter implements DocumentListener {

    private final Runnable callback;

    DocumentAdapter(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        callback.run();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        callback.run();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}
}
