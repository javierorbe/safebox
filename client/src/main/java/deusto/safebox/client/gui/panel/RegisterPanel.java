package deusto.safebox.client.gui.panel;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import static deusto.safebox.client.util.GuiUtil.addComponentInARow;
import static deusto.safebox.client.util.GuiUtil.addLabelFieldPanelInARow;

public class RegisterPanel extends JPanel {

    public RegisterPanel() {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        final LabelFieldPanel email = new LabelFieldPanel("Email", 30);
        final LabelFieldPanel username = new LabelFieldPanel("Username", 30);
        final LabelFieldPanel password = new LabelFieldPanel("Password", 30, true);
        final LabelFieldPanel rPassword = new LabelFieldPanel("Repeat password", 30, true);
        final JButton seePassword = new JButton();
        final JButton seerPassword = new JButton();
        final JButton registerBtn = new JButton("Register");

        gbc.insets = new Insets(4,4,4,4);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        addLabelFieldPanelInARow(this, email, gbc, 0);
        addLabelFieldPanelInARow(this, username, gbc, 1);
        addLabelFieldPanelInARow(this, password, gbc, 2);
        addLabelFieldPanelInARow(this, rPassword, gbc, 3);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridx = 0;
        gbc.gridwidth = 0;
        addComponentInARow(this, registerBtn, gbc, 4);

        gbc.gridx = 2;
        addComponentInARow(this, seePassword, gbc, 2);
        addComponentInARow(this, seerPassword, gbc, 3);
        //TODO: Fix position and size of seePassword and seerPassword
    }




}
