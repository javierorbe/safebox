package deusto.safebox.server.gui;

import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.RightAlignedLabel;
import deusto.safebox.common.gui.SimpleButton;
import deusto.safebox.server.dao.sql.SqlDaoManager;
import deusto.safebox.server.net.Server;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class ServerPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    ServerPanel(Server server, SqlDaoManager daoManager) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel serverState = new JLabel("Off");
        SimpleButton startBtn = new SimpleButton("Start server");
        SimpleButton stopBtn = new SimpleButton("Stop server");
        stopBtn.setEnabled(false);

        startBtn.addAction(() -> {
            if (!server.isRunning()) {
                startBtn.setEnabled(false);
                server.start();
                stopBtn.setEnabled(true);
                serverState.setText("On");
            }
        });

        stopBtn.addAction(() -> {
            if (server.isRunning()) {
                stopBtn.setEnabled(false);
                server.close();
                daoManager.close();
                serverState.setText("Off");
            }
        });

        JLabel clientCount = new JLabel("0");
        JLabel authClientCount = new JLabel("0");

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);

        addInfo("Server state:", serverState);
        addInfo("Socket server port:", new JLabel(String.valueOf(server.getPort())));
        addInfo("SQL server:", new JLabel(daoManager.getJdbcUrl()));
        addInfo("Clients:", clientCount);
        addInfo("Authenticated clients:", authClientCount);

        JPanel btnPanel = new JPanel();
        btnPanel.add(startBtn);
        btnPanel.add(stopBtn);
        gbb.setGridWidth(GridBagConstraints.REMAINDER)
                .setFillAndAnchor(GridBagBuilder.Fill.NONE, GridBagBuilder.Anchor.SOUTH);
        put(btnPanel);

        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> SwingUtilities.invokeLater(() -> {
                    clientCount.setText(String.valueOf(server.getClientCount()));
                    authClientCount.setText(String.valueOf(server.getAuthenticatedClientCount()));
                }), 5, 5, TimeUnit.SECONDS);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    private void addInfo(String labelName, JLabel info) {
        gbb.setGridWidthAndWeightX(1, 0);
        put(new RightAlignedLabel(labelName));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(info);
    }
}
