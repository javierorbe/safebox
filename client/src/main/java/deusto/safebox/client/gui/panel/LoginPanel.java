package deusto.safebox.client.gui.panel;

import static deusto.safebox.client.gui.GridBagBuilder.Anchor;
import static deusto.safebox.client.gui.GridBagBuilder.Fill;
import static deusto.safebox.client.util.IconManager.IconType;

import deusto.safebox.client.gui.GridBagBuilder;
import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.ShowPasswordField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class LoginPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    LoginPanel() {
        super(new GridBagLayout());

        final JTextField emailField = new JTextField(30);
        final ShowPasswordField passwordField = new ShowPasswordField(30, false);
        final JButton loginBtn = new JButton("Login");
        final JCheckBox rememberEmail = new JCheckBox("Remember Email");
        final ChangingToggleButton showPasswordBtn = new ChangingToggleButton(
                IconType.EYE, IconType.EYE_CLOSED, false) {
            @Override
            public void action(boolean state) {
                if (state) {
                    passwordField.showPassword();
                } else {
                    passwordField.hidePassword();
                }
            }
        };

        gbb.setInsets(4, 4, 4, 4);
        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        addGB(new JLabel("Email"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        addGB(emailField);

        gbb.setGridWidthAndWeightX(1, 0);
        addGB(new JLabel("Password"));
        gbb.setWeightX(1);
        addGB(passwordField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        addGB(showPasswordBtn);

        addGB(rememberEmail);

        gbb.setFillAndAnchor(Fill.NONE, Anchor.SOUTH);
        addGB(loginBtn);
    }

    private void addGB(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
