package deusto.safebox.client.gui.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextField;

/**
 * A {@link JTextField} with a placeholder text.
 */
class PlaceholderTextField extends JTextField {

    private final String placeholder;

    PlaceholderTextField(int columns, String placeholder) {
        super(columns);
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
