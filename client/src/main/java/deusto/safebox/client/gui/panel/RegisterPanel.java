package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;
import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType;
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
import deusto.safebox.common.net.packet.RequestRegisterPacket;
import deusto.safebox.common.net.packet.RetrieveDataPacket;
import deusto.safebox.common.net.packet.SuccessfulRegisterPacket;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RegisterPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterPanel.class);

    private final GridBagBuilder gbb = new GridBagBuilder();

    private final JTextField nameField = new LimitedTextField(50, true);
    private final JTextField emailField = new LimitedTextField(50, true);
    private final PasswordField pwdField = new PasswordField(100, false);
    private final PasswordField confirmPwdField = new PasswordField(100, false);
    private final JButton registerBtn;

    private final Consumer<RequestRegisterPacket> sendRegisterRequest;

    /**
     * Constructs a register panel.
     *
     * @param sendRegisterRequest callback to request a registration.
     */
    RegisterPanel(Consumer<RequestRegisterPacket> sendRegisterRequest) {
        super(new GridBagLayout());
        this.sendRegisterRequest = sendRegisterRequest;
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        registerBtn = new SimpleButton("Register", this::onRegister);
        ToggleButton showPwdBtn = new ToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                pwdField::showPassword,
                pwdField::hidePassword
        );
        ToggleButton showConfirmPwdBtn = new ToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                confirmPwdField::showPassword,
                confirmPwdField::hidePassword
        );

        addEnterAction(nameField, this::onRegister);
        addEnterAction(emailField, this::onRegister);
        addEnterAction(pwdField, this::onRegister);
        addEnterAction(confirmPwdField, this::onRegister);

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new RightAlignedLabel("Full Name:"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(nameField);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new RightAlignedLabel("Email:"));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(emailField);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new RightAlignedLabel("Password:"));
        gbb.setWeightX(1);
        put(pwdField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(showPwdBtn);

        gbb.setGridWidthAndWeightX(1, 0);
        put(new RightAlignedLabel("Confirm Password:"));
        gbb.setWeightX(1);
        put(confirmPwdField);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(showConfirmPwdBtn);

        gbb.setFillAndAnchor(Fill.NONE, Anchor.SOUTH);
        put(registerBtn);

        registerListeners();
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    private void registerListeners() {
        Runnable enableComponents = () -> runSwing(() -> setComponentsEnabled(true));

        PacketHandler.INSTANCE.registerListener(RetrieveDataPacket.class, p -> enableComponents.run());
        PacketHandler.INSTANCE.registerListener(SuccessfulRegisterPacket.class, p -> {
            clearFields().run();
            enableComponents.run();
            ToastDialog.showInfo(this, "Successfully registered.");
        });

        ErrorHandler.addListener(ErrorType.UNKNOWN_ERROR, enableComponents);
        ErrorHandler.addListener(ErrorType.EMAIL_ALREADY_IN_USE, () -> {
            enableComponents.run();
            ToastDialog.showError(this, "Email already in use.");
        });
    }

    private void onRegister() {
        setComponentsEnabled(false);

        String email = emailField.getText();
        if (TextValidator.EMAIL.isNotValid(email)) {
            setComponentsEnabled(true);
            ToastDialog.showError(this, "Invalid email address.");
            return;
        }

        String password = new String(pwdField.getPassword());
        String confirmPassword = new String(confirmPwdField.getPassword());

        if (password.isEmpty()) {
            setComponentsEnabled(true);
            LOGGER.error("Password empty");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerBtn.setEnabled(true);
            ToastDialog.showError(this, "The confirm password doesn't match the password.");
            return;
        }

        String name = nameField.getText();

        ClientSecurity.getAuthHash(email, password)
                .thenAccept(hash -> sendRegisterRequest.accept(new RequestRegisterPacket(name, email, hash)))
                .exceptionally(e -> {
                    LOGGER.error("Error generating auth hash.", e);
                    return null;
                });
    }

    private void setComponentsEnabled(boolean value) {
        nameField.setEnabled(value);
        emailField.setEnabled(value);
        pwdField.setEnabled(value);
        confirmPwdField.setEnabled(value);
        registerBtn.setEnabled(value);
    }

    private Runnable clearFields() {
        return () -> runSwing(() -> {
            nameField.setText("");
            emailField.setText("");
            pwdField.setText("");
            confirmPwdField.setText("");
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
