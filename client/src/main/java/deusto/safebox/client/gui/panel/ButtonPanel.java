package deusto.safebox.client.gui.panel;

import deusto.safebox.common.gui.SimpleButton;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

abstract class ButtonPanel extends JPanel {

    ButtonPanel() {
        super(new FlowLayout(FlowLayout.RIGHT));

        JButton acceptBtn = new SimpleButton("OK");
        JButton cancelBtn = new SimpleButton("Cancel");
        JButton applyBtn = new SimpleButton("Apply");

        acceptBtn.addActionListener(e -> accept());
        cancelBtn.addActionListener(e -> cancel());
        applyBtn.addActionListener(e -> apply());

        add(acceptBtn);
        add(cancelBtn);
        add(applyBtn);
    }

    abstract void accept();

    abstract void cancel();

    abstract void apply();

    // TODO: allow enabling/disabling the apply button
}
