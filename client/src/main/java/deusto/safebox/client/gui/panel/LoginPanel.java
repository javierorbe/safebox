package deusto.safebox.client.gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import static deusto.safebox.client.util.GuiUtil.addComponentInARow;
import static deusto.safebox.client.util.GuiUtil.addLabelFieldPanelInARow;

class LoginPanel extends JPanel {

    public LoginPanel() {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        final LabelFieldPanel email = new LabelFieldPanel("Email", 30);
        final LabelFieldPanel password = new LabelFieldPanel("Password",30, true);
        final JButton seePassword = new JButton();
        final JCheckBox rememberEmail = new JCheckBox("Remember email");
        final JButton loginBtn = new JButton("Login");

        gbc.insets = new Insets(4, 4, 4, 4);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        addLabelFieldPanelInARow(this, email, gbc, 0);
        addLabelFieldPanelInARow(this, password, gbc, 1);
        addComponentInARow(this, rememberEmail, gbc, 2);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridx = 0;
        gbc.gridwidth = 0;
        addComponentInARow(this, loginBtn, gbc, 3);

        gbc.gridx = 2;
        addComponentInARow(this, seePassword, gbc, 1);
        //TODO: Fix position and size of seePassword
    }
}
