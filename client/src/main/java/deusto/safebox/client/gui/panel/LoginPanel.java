package deusto.safebox.client.gui.panel;

import static deusto.safebox.client.util.IconManager.IconType;
import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.ShowPasswordField;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.SimpleButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
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

        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        final JTextField emailField = new JTextField();
        final ShowPasswordField passwordField = new ShowPasswordField(false);
        final JButton loginBtn = new SimpleButton("Login");
        final JCheckBox rememberEmail = new JCheckBox("Remember Email");
        rememberEmail.setFocusPainted(false);
        final ChangingToggleButton showPasswordBtn = new ChangingToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                passwordField::showPassword,
                passwordField::hidePassword
        );

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Email"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(emailField);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel("Password"));
        gbb.setWeightX(1);
        put(passwordField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(showPasswordBtn);

        gbb.setGridWidth(1);
        put(rememberEmail);

        gbb.incrementGridX()
                .setGridWidth(GridBagConstraints.REMAINDER)
                .setFillAndAnchor(Fill.NONE, Anchor.SOUTH);
        put(loginBtn);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
