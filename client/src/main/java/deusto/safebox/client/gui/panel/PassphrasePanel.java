package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;

import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

class PassphrasePanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    PassphrasePanel() {

        super(new GridBagLayout());

        JSlider sWordCount = new JSlider(JSlider.HORIZONTAL, 8, 30, 15);
        JLabel wCountLength = new JLabel(String.valueOf(sWordCount.getValue()));
        JTextField wSep = new JTextField();

        sWordCount.addChangeListener(e -> wCountLength.setText(String.valueOf(sWordCount.getValue())));

        gbb.setInsets(6,6,6,6);
        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Word Count: "));
        gbb.setGridWidthAndWeightX(GridBagConstraints.RELATIVE, 1);
        put(sWordCount);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(wCountLength);
        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Word Separator: "));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(wSep);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
