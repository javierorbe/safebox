package deusto.safebox.client.gui.panel;

import deusto.safebox.client.util.GuiUtil;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

public class SettingsDialog extends JDialog {

    public SettingsDialog(JFrame owner) {
        super(owner, "Settings", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(720, 480));
        setLayout(new GridBagLayout());

        String[] settingsList = {
            "General",
            "Appearance"
        };

        JTree settingsTree = new JTree(settingsList);
        JScrollPane settingsScrollPane = new JScrollPane(settingsTree);

        JPanel mainSettingsPanel = new JPanel(new FlowLayout());
        mainSettingsPanel.add(new JLabel("Settings..."));

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, settingsScrollPane, mainSettingsPanel);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(new JButton("OK"));
        btnPanel.add(new JButton("Cancel"));
        btnPanel.add(new JButton("Apply"));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        c.weightx = 0.5;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        add(splitPane, c);

        c.ipady = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.PAGE_END;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridy = 1;
        // c.insets = new Insets(5, 0, 0, 0);
        add(btnPanel, c);

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }
}
