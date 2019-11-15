package deusto.safebox.client.gui.panel;

import deusto.safebox.common.net.packet.RequestLoginPacket;
import deusto.safebox.common.net.packet.RequestRegisterPacket;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 * Authentication panel.
 * Contains the login and register panels.
 */
public class AuthPanel extends JPanel {

    /**
     * Creates a new authentication panel.
     *
     * @param loginRequest login request handler.
     * @param registerRequest register request handler.
     */
    public AuthPanel(Consumer<RequestLoginPacket> loginRequest,
                     Consumer<RequestRegisterPacket> registerRequest) {
        super(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
        tabbedPane.setRequestFocusEnabled(false);

        tabbedPane.addTab("Login", new LoginPanel(loginRequest));
        tabbedPane.addTab("Register", new RegisterPanel(registerRequest));

        add(tabbedPane, BorderLayout.CENTER);
    }
}
