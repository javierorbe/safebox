package deusto.safebox.server.gui;

import deusto.safebox.common.util.GuiUtil;
import deusto.safebox.server.dao.sql.SqlDaoManager;
import deusto.safebox.server.net.Server;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class ServerFrame extends JFrame {

    public ServerFrame(Server server, SqlDaoManager daoManager) {
        super("SafeBox Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GuiUtil.setDefaultLookAndFeel();
        setLayout(new BorderLayout());

        getContentPane().add(new ServerPanel(server, daoManager), BorderLayout.CENTER);

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }
}
