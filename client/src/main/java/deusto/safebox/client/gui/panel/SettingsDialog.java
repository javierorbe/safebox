package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.util.GuiUtil;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

public class SettingsDialog extends JDialog {

    private final GridBagBuilder gbb = new GridBagBuilder();

    public SettingsDialog(JFrame owner) {
        super(owner, "Settings", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(720, 480));
        setLayout(new GridBagLayout());

        JPanel mainSettingsPanel = new JPanel(new FlowLayout());

        {
            JPanel startup = new JPanel();
            startup.setLayout(new BoxLayout(startup, BoxLayout.Y_AXIS));
            startup.setBorder(BorderFactory.createTitledBorder("Startup"));
            startup.add(new JCheckBox("Start only a single instance of SafeBox"));
            startup.add(new JCheckBox("Minimize window at application startup"));
            mainSettingsPanel.add(startup);
        }

        String[] settingsList = {
            "General",
            "Appearance"
        };

        JTree settingsTree = new JTree(settingsList);
        JScrollPane settingsScrollPane = new JScrollPane(settingsTree);
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, settingsScrollPane, mainSettingsPanel);

        gbb.setFill(Fill.BOTH);

        gbb.setWeight(0.5, 1.0);
        gbb.setGridY(0);
        put(splitPane);

        gbb.setWeightY(0);
        gbb.setAnchor(Anchor.PAGE_END);
        gbb.setGridWidthAndWeightX(0, GridBagConstraints.REMAINDER);
        gbb.incrementGridY();

        put(new ButtonPanel(
            () -> { /* TODO */ },
            () -> { /* TODO */ },
            () -> { /* TODO */ }
        ));

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
