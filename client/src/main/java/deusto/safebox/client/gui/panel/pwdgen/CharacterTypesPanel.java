package deusto.safebox.client.gui.panel.pwdgen;

import deusto.safebox.common.gui.GridBagBuilder;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import java.awt.GridBagLayout;

class CharacterTypesPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    CharacterTypesPanel() {

        //setLayout(new FlowLayout(FlowLayout.LEFT));

        super(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Character Types");
        setBorder(border);

        JToggleButton upper = new JToggleButton("A-Z");
        JToggleButton lower = new JToggleButton("a-z");
        JToggleButton numbers = new JToggleButton("0-9");
        JToggleButton symbols = new JToggleButton("/*_...");
        JToggleButton extendedAscii = new JToggleButton("Extended ASCII");

        //TODO: Adds listeners of JToggleButtons

        /*
        add(upper);
        add(lower);
        add(numbers);
        add(symbols);
        add(extendedAscii);
        */
        gbb.setInsets(6,6,6,6);
        put(upper);
        put(lower);
        put(numbers);
        put(symbols);
        put(extendedAscii);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
