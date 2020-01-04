package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.util.GuiUtil.runSwing;

import deusto.safebox.client.gui.component.LimitedTextField;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.gui.component.ToggleButton;
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoginPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPanel.class);

    private final GridBagBuilder gbb = new GridBagBuilder();

    private final JTextField emailField = new LimitedTextField(50, true);
    private final PasswordField passwordField = new PasswordField(100, false);
    private final JCheckBox rememberEmail = new JCheckBox("Remember Email");
    private final JButton loginBtn;

    private final Consumer<RequestLoginPacket> sendLoginRequest;

    /**
     * Constructs a login panel.
     *
     * @param sendLoginRequest callback to request a login.
     */
    LoginPanel(Consumer<RequestLoginPacket> sendLoginRequest) {
        super(new GridBagLayout());
        this.sendLoginRequest = sendLoginRequest;
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        rememberEmail.setFocusPainted(false);
        loginBtn = new SimpleButton("Login", this::onLogin);
        ToggleButton showPasswordBtn = new ToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                passwordField::showPassword,
                passwordField::hidePassword
        );

        addEnterAction(emailField, this::onLogin);
        addEnterAction(passwordField, this::onLogin);

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

        registerListeners();
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    private void registerListeners() {
        Runnable enableComponents = () -> runSwing(() -> setComponentsEnabled(true));

        PacketHandler.INSTANCE.registerListener(RetrieveDataPacket.class, p -> {
            clearFields().run();
            enableComponents.run();
        });

        ErrorHandler.addListener(ErrorPacket.ErrorType.UNKNOWN_ERROR, () -> {
            clearFields().run();
            enableComponents.run();
        });
        ErrorHandler.addListener(ErrorPacket.ErrorType.INVALID_LOGIN, () -> {
            clearFields().run();
            enableComponents.run();
            ToastDialog.showError(this, "Invalid login details.");
        });
    }

    private void onLogin() {
        setComponentsEnabled(false);

        String email = emailField.getText();
        if (TextValidator.EMAIL.isNotValid(email)) {
            setComponentsEnabled(true);
            ToastDialog.showError(this, "Invalid email address.");
            return;
        }

        String password = new String(passwordField.getPassword());

        if (password.isEmpty()) {
            setComponentsEnabled(true);
            LOGGER.error("Password empty");
            return;
        }

        ClientSecurity.getAuthHash(email, password)
                .thenAccept(hash -> sendLoginRequest.accept(new RequestLoginPacket(email, hash)))
                .exceptionally(e -> {
                    LOGGER.error("Error generating auth hash.", e);
                    ToastDialog.showError(this, "Unknown error.");
                    return null;
                });
    }

    private void setComponentsEnabled(boolean value) {
        emailField.setEnabled(value);
        passwordField.setEnabled(value);
        loginBtn.setEnabled(value);
        emailField.setEnabled(value);
    }

    private Runnable clearFields() {
        return () -> runSwing(() -> {
            passwordField.setText("");

            if (!rememberEmail.isSelected()) {
                emailField.setText("");
            }
        });
    }

    private void addEnterAction(JTextField field, Runnable runnable) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    runnable.run();
                }
            }
        });
    }
}
