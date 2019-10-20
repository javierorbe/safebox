package deusto.safebox.client.util;
import deusto.safebox.client.gui.panel.LabelFieldPanel;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.Toolkit;

public class GuiUtil {

    private GuiUtil() {
        throw new AssertionError();
    }

    public static Point getCenteredLocation(Component component) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(
                dimension.width / 2 - component.getWidth() / 2,
                dimension.height / 2 - component.getHeight() / 2
        );
    }

    public static void addComponentInARow(JPanel panel, JComponent component, GridBagConstraints gbc, int gridy) {
        gbc.gridy = gridy;
        panel.add(component, gbc);
    }

    public static void addLabelFieldPanelInARow(JPanel panel, LabelFieldPanel l, GridBagConstraints gbc, int gridy) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        addComponentInARow(panel, l.getLabel(), gbc, gridy);
        gbc.gridx = 1;
        gbc.weightx = 1;
        addComponentInARow(panel, l.getTextField(), gbc, gridy);
    }
}
