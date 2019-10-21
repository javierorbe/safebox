package deusto.safebox.client.gui.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * A {@link JTextField} with a placeholder text.
 */
public class PlaceholderTextField extends JTextField {

    private String placeholder = "";

    public PlaceholderTextField() {}

    public PlaceholderTextField(String text) {
        super(text);
    }

    public PlaceholderTextField(int columns) {
        super(columns);
    }

    public PlaceholderTextField(String text, int columns) {
        super(text, columns);
    }

    public PlaceholderTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        if (placeholder == null || placeholder.isEmpty() || !getText().isEmpty()) {
            return;
        }

        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getDisabledTextColor());

        int padding = (getHeight() - getFont().getSize()) / 2;
        g2d.drawString(placeholder, getInsets().left, getHeight() - padding - 1);
        // g2d.drawString(placeholder, getInsets().left, g.getFontMetrics().getHeight() + getInsets().top);
    }
}
