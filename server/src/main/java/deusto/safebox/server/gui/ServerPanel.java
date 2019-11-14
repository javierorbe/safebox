package deusto.safebox.server.gui;

import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.SimpleButton;
import deusto.safebox.server.dao.sql.SqlDaoManager;
import deusto.safebox.server.net.Server;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ServerPanel extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(ServerFrame.class);

    private final SqlDaoManager daoManager;
    private final GridBagBuilder gbb = new GridBagBuilder();

    ServerPanel(Server server, SqlDaoManager daoManager) {
        super(new GridBagLayout());
        this.daoManager = daoManager;
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // TODO: add socket list

        JLabel serverState = new JLabel("Off");
        JLabel socketServerPort = new JLabel(String.valueOf(server.getPort()));
        String sqlServerUrl = getSqlServerUrl().orElse("unknown");
        JLabel sqlServer = new JLabel(sqlServerUrl);

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

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);

        addInfo("Server state: ", serverState);
        addInfo("Socket server port: ", socketServerPort);
        addInfo("SQL server: ", sqlServer);

        JPanel btnPanel = new JPanel();
        btnPanel.add(startBtn);
        btnPanel.add(stopBtn);
        gbb.setGridWidth(GridBagConstraints.REMAINDER)
                .setFillAndAnchor(GridBagBuilder.Fill.NONE, GridBagBuilder.Anchor.SOUTH);
        put(btnPanel);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    private void addInfo(String labelName, JLabel info) {
        gbb.setGridWidthAndWeightX(1, 0);
        put(new JLabel(labelName));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
        put(info);
    }

    private Optional<String> getSqlServerUrl() {
        try {
            return Optional.of(daoManager.getDatabaseMetadata().getURL());
        } catch (SQLException e) {
            logger.warn("Could not get the database URL.", e);
            return Optional.empty();
        }
    }
}
