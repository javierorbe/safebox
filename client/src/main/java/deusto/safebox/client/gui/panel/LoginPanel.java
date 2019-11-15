package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.LimitedTextField;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.net.ErrorHandler;
import deusto.safebox.client.util.IconType;
import deusto.safebox.client.util.TextValidator;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.SimpleButton;
import deusto.safebox.common.net.packet.ErrorPacket;
import deusto.safebox.common.net.packet.RequestLoginPacket;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

class LoginPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    private final JTextField emailField = new LimitedTextField(50, true);
    private final PasswordField passwordField = new PasswordField(100, false);
    private final JCheckBox rememberEmail = new JCheckBox("Remember Email");
    private final JButton loginBtn;

    private final Consumer<RequestLoginPacket> sendLoginRequest;

    LoginPanel(Consumer<RequestLoginPacket> sendLoginRequest) {
        super(new GridBagLayout());
        this.sendLoginRequest = sendLoginRequest;
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        rememberEmail.setFocusPainted(false);
        loginBtn = new SimpleButton("Login", this::loginAction);
        ChangingToggleButton showPasswordBtn = new ChangingToggleButton(
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

        ErrorHandler.INSTANCE.addListener(ErrorPacket.ErrorType.INVALID_LOGIN,
                () -> SwingUtilities.invokeLater(() -> loginBtn.setEnabled(true)));
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    private void loginAction() {
        loginBtn.setEnabled(false);

        String email = emailField.getText();
        if (!TextValidator.EMAIL.isValid(email)) {
            loginBtn.setEnabled(true);
            // TODO: show error message
            return;
        }

        new Thread(() -> {
            if (rememberEmail.isSelected()) {
                // TODO
            }

            String password = new String(passwordField.getPassword());
            // TODO: send the login request
        }).start();
    }
}
