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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    public RegisterPanel() {
        super(new GridBagLayout());

        final JTextField nameField  = new JTextField(30);
        final JTextField emailField = new JTextField(30);
        final ShowPasswordField passwordField = new ShowPasswordField(30, false);
        final ShowPasswordField rPasswordField = new ShowPasswordField(30, false);
        final JButton registerBtn = new JButton("Register");
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
        final ChangingToggleButton showRPasswordBtn = new ChangingToggleButton(
                IconType.EYE, IconType.EYE_CLOSED, false) {
            @Override
            public void action(boolean state) {
                if (state) {
                    rPasswordField.showPassword();
                } else {
                    rPasswordField.hidePassword();
                }
            }
        };

        gbb.setInsets(4, 4, 4, 4);
        gbb.setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 0);
        addGB(new JLabel("Name"));
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
        addGB(new JLabel("Repeat password"));
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

    /*
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(500, 500));

        LoginPanel lP = new LoginPanel();
        RegisterPanel rP = new RegisterPanel();

        JTabbedPane tb = new JTabbedPane();
        tb.addTab("Login", lP);
        tb.addTab("Register", rP);

        frame.add(tb);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    */

}
