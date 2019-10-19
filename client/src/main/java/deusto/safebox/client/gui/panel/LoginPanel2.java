package deusto.safebox.client.gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class LoginPanel2 extends JPanel {

    LoginPanel2() {
        super(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        final JLabel emailLabel = new JLabel("Email");
        final JLabel passwordLabel = new JLabel("Password");
        final JTextField emailField = new JTextField(30);
        final JPasswordField passwordField = new JPasswordField(30);
        final JButton loginBtn = new JButton("Login");

        c.insets = new Insets(4, 4, 4, 4);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 1;
        c.weightx = 0;
        add(emailLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        add(emailField, c);

        c.gridwidth = 1;
        c.weightx = 0;
        add(passwordLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        add(passwordField, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTH;
        add(loginBtn, c);
    }
}
