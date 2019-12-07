package deusto.safebox.client.gui.panel.pwdgen;

import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.DefaultEditorKit;
import java.awt.GridBagLayout;

class PassOptionsPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    final JButton close = new JButton("Close");
    final JButton regenerate = new JButton("Regenerate");
    final JButton copy = new JButton("Copy");

    PassOptionsPanel() {

        super(new GridBagLayout());

        //TODO: Adds listeners of JButtons

        gbb.setInsets(6,6,6,6);

        gbb.setGridWidthAndWeightX(0, 0);
        gbb.setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.EAST);
        put(regenerate);
        gbb.setGridY(1);
        put(copy);
        gbb.setGridY(2);
        put(close);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }


}
