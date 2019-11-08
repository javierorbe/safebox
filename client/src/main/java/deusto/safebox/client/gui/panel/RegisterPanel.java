package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.ShowPasswordField;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.gui.GridBagBuilder;
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
        final ShowPasswordField pwdField = new ShowPasswordField(false);
        final ShowPasswordField confirmPwdField = new ShowPasswordField(false);
        final JButton registerBtn = new SimpleButton("Register");
        final ChangingToggleButton showPasswordBtn = new ChangingToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                pwdField::showPassword,
                pwdField::hidePassword
        );
        final ChangingToggleButton showRPasswordBtn = new ChangingToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                confirmPwdField::showPassword,
                confirmPwdField::hidePassword
        );

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Full Name"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(nameField);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Email"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(emailField);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Password"));
        gbb.setWeightX(1);
        put(pwdField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(showPasswordBtn);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Confirm Password"));
        gbb.setWeightX(1);
        put(confirmPwdField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(showRPasswordBtn);

        gbb.setFillAndAnchor(Fill.NONE, Anchor.SOUTH);
        put(registerBtn);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
