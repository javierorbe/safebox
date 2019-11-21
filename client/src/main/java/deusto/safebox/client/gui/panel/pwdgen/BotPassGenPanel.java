package deusto.safebox.client.gui.panel.pwdgen;

import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

class BotPassGenPanel extends JPanel{

    private final GridBagBuilder gbb = new GridBagBuilder();
    final PassOptionsPanel optionsPanel = new PassOptionsPanel();

    BotPassGenPanel() {

        super(new GridBagLayout());

        final JTabbedPane tabbedPane = new JTabbedPane();
        final PassPanel passPanel = new PassPanel();
        final PassphrasePanel passphrasePanel = new PassphrasePanel();


        tabbedPane.addTab("Password", passPanel);
        tabbedPane.addTab("Passphrase", passphrasePanel);

        gbb.setInsets(6,6,6,6);

        gbb.setGridX(0).setGridY(0);
        gbb.setGridWidthAndWeightX(1, 1);
        gbb.setGridHeight(3);
        gbb.setFillAndAnchor(GridBagBuilder.Fill.BOTH, GridBagBuilder.Anchor.CENTER);
        put(tabbedPane);

        gbb.setGridX(1);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(optionsPanel);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
