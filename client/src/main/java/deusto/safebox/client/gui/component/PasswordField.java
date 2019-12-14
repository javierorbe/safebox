package deusto.safebox.client.gui.component;

import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A {@link JPasswordField} where the password can be shown or hidden.
 */
public class PasswordField extends JPasswordField {

    private static final char HIDDEN_CHAR = '●';

    /**
     * Creates a {@link PasswordField} with the specified column count.
     *
     * @param document the text document of the field.
     * @param initialPassword the initial password.
     * @param show if true, the password is initially shown;
     *             otherwise, the password is initially hidden.
     */
    private PasswordField(Document document, String initialPassword, boolean show) {
        super(document, initialPassword, 0);

        if (show) {
            showPassword();
        }
    }

    /**
     * Creates a {@link PasswordField} with the specified column count.
     *
     * @param limit password size limit.
     * @param initialPassword the initial password.
     * @param show if true, the password is initially shown;
     *             otherwise, the password is initially hidden.
     */
    public PasswordField(int limit, String initialPassword, boolean show) {
        this(new LimitedDocument(limit, true), initialPassword, show);
    }

    public PasswordField(int limit, boolean show) {
        this(limit, null, show);
    }

    public PasswordField(String initialPassword, boolean show) {
        this(null, initialPassword, show);
    }

    public void showPassword() {
        setEchoChar((char) 0);
        putClientProperty("JPasswordField.cutCopyAllowed", true);
    }

    public void hidePassword() {
        setEchoChar(HIDDEN_CHAR);
        putClientProperty("JPasswordField.cutCopyAllowed", false);
    }

    /**
     * Installs a listener to receive notification when the text of any
     * {@code JTextComponent} is changed. Internally, it installs a
     * {@link DocumentListener} on the text component's {@link Document},
     * and a {@link PropertyChangeListener} on the text component to detect
     * if the {@code Document} itself is replaced.
     *
     * @param changeListener a listener to receieve {@link ChangeEvent}s
     *        when the text is changed; the source object for the events
     *        will be the text component
     * @throws NullPointerException if either parameter is null
     */
    public void addChangeListener(ChangeListener changeListener) {
        DocumentListener dl = new DocumentListener() {
            private int lastChange = 0, lastNotifiedChange = 0;

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lastChange++;
                SwingUtilities.invokeLater(() -> {
                    if (lastNotifiedChange != lastChange) {
                        lastNotifiedChange = lastChange;
                        changeListener.stateChanged(new ChangeEvent(this));
                    }
                });
            }
        };

        this.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
            Document d1 = (Document)e.getOldValue();
            Document d2 = (Document)e.getNewValue();
            d1.removeDocumentListener(dl);
            d2.addDocumentListener(dl);
            dl.changedUpdate(null);
        });

        Document d = this.getDocument();
        d.addDocumentListener(dl);
    }
}
