package deusto.safebox.client.gui.panel;

import deusto.safebox.common.gui.SimpleButton;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

class ButtonPanel extends JPanel {

    ButtonPanel(Runnable acceptAction, Runnable cancelAction, Runnable applyAction) {
        super(new FlowLayout(FlowLayout.RIGHT));

        JButton acceptBtn = new SimpleButton("OK");
        JButton cancelBtn = new SimpleButton("Cancel");
        JButton applyBtn = new SimpleButton("Apply");

        acceptBtn.addActionListener(e -> acceptAction.run());
        cancelBtn.addActionListener(e -> cancelAction.run());
        applyBtn.addActionListener(e -> applyAction.run());

        add(acceptBtn);
        add(cancelBtn);
        add(applyBtn);
    }

    // TODO: allow enabling/disabling the apply button
}
