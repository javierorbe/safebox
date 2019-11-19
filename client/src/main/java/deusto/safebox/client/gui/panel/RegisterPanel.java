package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.gui.component.ChangingToggleButton;
import deusto.safebox.client.gui.component.LimitedTextField;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.gui.component.RightAlignedLabel;
import deusto.safebox.client.net.ErrorHandler;
import deusto.safebox.client.net.PacketHandler;
import deusto.safebox.client.security.ClientSecurity;
import deusto.safebox.client.util.IconType;
import deusto.safebox.client.util.TextValidator;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.SimpleButton;
import deusto.safebox.common.net.packet.ErrorPacket;
import deusto.safebox.common.net.packet.ReceiveDataPacket;
import deusto.safebox.common.net.packet.RequestRegisterPacket;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

class RegisterPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    private final JTextField nameField = new LimitedTextField(50, true);
    private final JTextField emailField = new LimitedTextField(50, true);
    private final PasswordField pwdField = new PasswordField(100, false);
    private final PasswordField confirmPwdField = new PasswordField(100, false);
    private final JButton registerBtn;

    private final Consumer<RequestRegisterPacket> sendRegisterRequest;

    RegisterPanel(Consumer<RequestRegisterPacket> sendRegisterRequest) {
        super(new GridBagLayout());
        this.sendRegisterRequest = sendRegisterRequest;
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        registerBtn = new SimpleButton("Register", this::registerAction);
        ChangingToggleButton showPwdBtn = new ChangingToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                pwdField::showPassword,
                pwdField::hidePassword
        );
        ChangingToggleButton showConfirmPwdBtn = new ChangingToggleButton(
                IconType.EYE,
                IconType.EYE_CLOSED,
                false,
                confirmPwdField::showPassword,
                confirmPwdField::hidePassword
        );

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

        Runnable enableRegisterBtn = () -> SwingUtilities.invokeLater(() -> registerBtn.setEnabled(true));
        ErrorHandler.INSTANCE.addListener(ErrorPacket.ErrorType.EMAIL_ALREADY_IN_USE,
                () -> SwingUtilities.invokeLater(() -> {
                    registerBtn.setEnabled(true);
                    new ToastDialog("Email already in use.", Color.RED, 2, this);
                }));
        ErrorHandler.INSTANCE.addListener(ErrorPacket.ErrorType.UNKNOWN_ERROR, enableRegisterBtn);
        PacketHandler.INSTANCE.addListener(ReceiveDataPacket.class, ignored -> enableRegisterBtn.run());
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    private void registerAction() {
        registerBtn.setEnabled(false);

        String email = emailField.getText();
        if (!TextValidator.EMAIL.isValid(email)) {
            registerBtn.setEnabled(true);
            new ToastDialog("Invalid email address.", Color.RED, 2, this);
            return;
        }

        String password = new String(pwdField.getPassword());
        String confirmPassword = new String(confirmPwdField.getPassword());

        if (!password.equals(confirmPassword)) {
            registerBtn.setEnabled(true);
            new ToastDialog("The confirm password doesn't match the password.", Color.RED, 2, this);
            return;
        }

        String name = nameField.getText();

        new Thread(() -> {
            String passwordHash = ClientSecurity.getAuthHash(email, password);
            sendRegisterRequest.accept(new RequestRegisterPacket(name, email, passwordHash));
        }).start();
    }
}
