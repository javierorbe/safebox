package deusto.safebox.client.gui.panel.pwdgen;

import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;

import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

class PassGenPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();
    final BotPassGenPanel botPassGenPanel = new BotPassGenPanel();

    PassGenPanel() {

        super(new GridBagLayout());

        final TopPassGenPanel topPassGenPanel = new TopPassGenPanel();

        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(Fill.BOTH, Anchor.CENTER);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(topPassGenPanel);
        put(botPassGenPanel);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
