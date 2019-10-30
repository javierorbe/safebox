package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.client.util.IconManager.IconType;

import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.ShowPasswordField;
import deusto.safebox.common.gui.SimpleButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class RegisterPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    RegisterPanel() {
        super(new GridBagLayout());

        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        final JTextField nameField  = new JTextField();
        final JTextField emailField = new JTextField();
        final ShowPasswordField passwordField = new ShowPasswordField(false);
        final ShowPasswordField rPasswordField = new ShowPasswordField(false);
        final JButton registerBtn = new SimpleButton("Register");
        final ChangingToggleButton showPasswordBtn = new ChangingToggleButton(
                IconType.EYE, IconType.EYE_CLOSED, false) {
            @Override
            public void on() {
                passwordField.showPassword();
            }

            @Override
            public void off() {
                passwordField.hidePassword();
            }
        };
        final ChangingToggleButton showRPasswordBtn = new ChangingToggleButton(
                IconType.EYE, IconType.EYE_CLOSED, false) {
            @Override
            public void on() {
                rPasswordField.showPassword();
            }

            @Override
            public void off() {
                rPasswordField.hidePassword();
            }
        };

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        addGB(new JLabel("Full Name"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        addGB(nameField);

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

        gbb.setGridWidthAndWeightX(1, 0);
        addGB(new JLabel("Confirm Password"));
        gbb.setWeightX(1);
        addGB(rPasswordField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        addGB(showRPasswordBtn);

        gbb.setFillAndAnchor(Fill.NONE, Anchor.SOUTH);
        addGB(registerBtn);
    }

    private void addGB(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
