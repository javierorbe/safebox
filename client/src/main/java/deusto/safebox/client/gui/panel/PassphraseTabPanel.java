package deusto.safebox.client.gui.panel;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import deusto.safebox.common.gui.GridBagBuilder;

import javax.swing.*;
import java.awt.*;

public class PassphraseTabPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    PassphraseTabPanel() {

        super(new GridBagLayout());

        JSlider sPassLength = new JSlider(JSlider.HORIZONTAL, 8, 30, 15);
        JLabel pLength = new JLabel(String.valueOf(sPassLength.getValue()));
        JTextField wSep = new JTextField();

        sPassLength.addChangeListener(e -> pLength.setText(String.valueOf(sPassLength.getValue())));

        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Password's length"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.RELATIVE, 1);
        put(sPassLength);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(pLength);
        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Word Separator"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(wSep);

    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
