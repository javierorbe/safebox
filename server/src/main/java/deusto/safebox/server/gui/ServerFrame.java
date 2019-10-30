package deusto.safebox.server.gui;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.SimpleButton;
import deusto.safebox.common.util.GuiUtil;
import deusto.safebox.server.dao.sql.SqlDaoManager;
import deusto.safebox.server.net.Server;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerFrame extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(ServerFrame.class);

    private static final Dimension PREFERRED_SIZE = new Dimension(720, 480);

    private final GridBagBuilder gbb = new GridBagBuilder();

    public ServerFrame(Server server, SqlDaoManager daoManager) {
        super("SafeBox Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GuiUtil.setDefaultLookAndFeel();
        setPreferredSize(PREFERRED_SIZE);
        setLayout(new GridBagLayout());

        String sqlServerUrl;
        try {
            sqlServerUrl = daoManager.getDatabaseMetadata().getURL();
        } catch (SQLException e) {
            logger.warn("Could not get the database URL.", e);
            sqlServerUrl = "Unknown";
        }

        // TODO: add socket list

        final JLabel serverState = new JLabel("Server state: not running");
        final JLabel portLabel = new JLabel("Port: " + server.getPort());
        final JLabel sqlServer = new JLabel("SQL Server: " + sqlServerUrl);
        final JButton startBtn = new SimpleButton("Start server");
        final JButton stopBtn = new SimpleButton("Stop server");
        stopBtn.setEnabled(false);

        startBtn.addActionListener(e -> {
            if (!server.isRunning()) {
                startBtn.setEnabled(false);
                server.start();
                stopBtn.setEnabled(true);
                serverState.setText("Server state: running");
            }
        });

        stopBtn.addActionListener(e -> {
            if (server.isRunning()) {
                stopBtn.setEnabled(false);
                server.close();
                // startBtn.setEnabled(true);
                serverState.setText("Server state: not running");
            }
        });

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);

        put(serverState);
        put(portLabel);
        put(sqlServer);

        gbb.setGridWidth(1);
        put(startBtn);
        gbb.setGridWidth(GridBagConstraints.REMAINDER)
                .setFillAndAnchor(Fill.NONE, Anchor.SOUTH);
        put(stopBtn);

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }

    private void put(JComponent component) {
        getContentPane().add(component, gbb.getConstraints());
    }
}
