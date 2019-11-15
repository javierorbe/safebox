package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;

import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class PassGenPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    PassGenPanel() {

        super(new GridBagLayout());

        final TopPassGenPanel topPassGenPanel = new TopPassGenPanel();
        final BotPassGenPanel botPassGenPanel = new BotPassGenPanel();

        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(Fill.BOTH, Anchor.CENTER);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(topPassGenPanel);
        put(botPassGenPanel);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(new Dimension(500,400));
        f.add(new PassGenPanel());
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
