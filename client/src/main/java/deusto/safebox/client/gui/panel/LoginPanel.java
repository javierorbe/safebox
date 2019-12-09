package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.gui.component.ToggleButton;
import deusto.safebox.client.gui.component.LimitedTextField;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.net.ErrorHandler;
import deusto.safebox.client.net.PacketHandler;
import deusto.safebox.client.security.ClientSecurity;
import deusto.safebox.client.util.IconType;
import deusto.safebox.client.util.TextValidator;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.RightAlignedLabel;
import deusto.safebox.common.gui.SimpleButton;
import deusto.safebox.common.net.packet.ErrorPacket;
import deusto.safebox.common.net.packet.RequestLoginPacket;
import deusto.safebox.common.net.packet.RetrieveDataPacket;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoginPanel extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(LoginPanel.class);

    // TODO: once logged in, clean the password field

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
        ToggleButton showPasswordBtn = new ToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                passwordField::showPassword,
                passwordField::hidePassword
        );

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new RightAlignedLabel("Email:"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(emailField);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new RightAlignedLabel("Password:"));
        gbb.setWeightX(1);
        put(passwordField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(showPasswordBtn);

        gbb.setGridWidth(1)
                .setGridX(1);
        put(rememberEmail);

        gbb.setGridX(0)
                .setGridWidth(GridBagConstraints.REMAINDER)
                .setFillAndAnchor(Fill.NONE, Anchor.SOUTH);
        put(loginBtn);

        Runnable enableLoginBtn = () -> SwingUtilities.invokeLater(() -> loginBtn.setEnabled(true));
        PacketHandler.INSTANCE.registerListener(RetrieveDataPacket.class, e -> enableLoginBtn.run());
        ErrorHandler.addListener(ErrorPacket.ErrorType.UNKNOWN_ERROR, enableLoginBtn);
        ErrorHandler.addListener(ErrorPacket.ErrorType.INVALID_LOGIN, () -> SwingUtilities.invokeLater(() -> {
            loginBtn.setEnabled(true);
            new ToastDialog("Invalid login details.", Color.RED, 2, this);
        }));
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    private void loginAction() {
        loginBtn.setEnabled(false);

        String email = emailField.getText();
        if (TextValidator.EMAIL.isNotValid(email)) {
            loginBtn.setEnabled(true);
            new ToastDialog("Invalid email address.", Color.RED, 2, this);
            return;
        }

        if (rememberEmail.isSelected()) {
            // TODO
        }

        String password = new String(passwordField.getPassword());
        ClientSecurity.getAuthHash(email, password)
                .thenAccept(hash -> sendLoginRequest.accept(new RequestLoginPacket(email, hash)))
                .exceptionally(e -> {
                    logger.error("Error generating auth hash.", e);
                    return null;
                });
    }
}
