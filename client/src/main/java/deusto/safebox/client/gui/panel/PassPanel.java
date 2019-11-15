package deusto.safebox.client.gui.panel;

import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

class PassPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    PassPanel() {

        super(new GridBagLayout());

        final JSlider sPassLength = new JSlider(JSlider.HORIZONTAL, 8, 30, 15);
        final JLabel pLength = new JLabel(String.valueOf(sPassLength.getValue()));
        final CharacterTypesPanel charTypesPan = new CharacterTypesPanel();

        sPassLength.addChangeListener(e -> pLength.setText(String.valueOf(sPassLength.getValue())));

        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Length: "));
        gbb.setGridWidthAndWeightX(GridBagConstraints.RELATIVE, 1);
        put(sPassLength);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(pLength);
        put(charTypesPan);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
