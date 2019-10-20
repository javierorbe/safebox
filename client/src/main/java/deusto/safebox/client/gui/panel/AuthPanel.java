package deusto.safebox.client.gui.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class AuthPanel extends JPanel {

    public AuthPanel() {
        super(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
        tabbedPane.setRequestFocusEnabled(false);

        tabbedPane.addTab("Login", new LoginPanel());
        tabbedPane.addTab("Register", new RegisterPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
