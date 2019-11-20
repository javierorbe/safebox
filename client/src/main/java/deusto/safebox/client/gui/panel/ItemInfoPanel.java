package deusto.safebox.client.gui.panel;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.common.gui.GridBagBuilder;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.format.DateTimeFormatter;

class ItemInfoPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    ItemInfoPanel(LeafItem item) {
        super(new GridBagLayout());
        final JLabel type = new JLabel(item.getType().getName());
        final JLabel name = new JLabel(item.getName());
        final JLabel folder = new JLabel(item.getFolder().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final JLabel created = new JLabel(item.getCreated().format(formatter));
        final JLabel lastModified = new JLabel(item.getLastModified().format(formatter));

        gbb.setInsets(6, 6, 6, 6);
        gbb.setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);

        gbb.setGridWidthAndWeightX(1, 1);
        put(new JLabel("Name: "));
        gbb.setGridWidth(GridBagConstraints.REMAINDER);
        put(name);
        gbb.setGridWidth(1);
        put(new JLabel("Folder: "));
        gbb.setGridWidth(GridBagConstraints.REMAINDER);
        put(folder);
        gbb.setGridWidth(1);
        put(new JLabel("Created: "));
        gbb.setGridWidth(GridBagConstraints.REMAINDER);
        put(created);
        gbb.setGridWidth(1);
        put(new JLabel("Last modified: "));
        gbb.setGridWidth(GridBagConstraints.REMAINDER);
        put(lastModified);
        put(panelByItemType(item));
    }
    private JPanel panelByItemType(LeafItem item) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        item.getFeatures().forEach(feature -> {
            gbb.setGridWidth(1);
            put(new JLabel((String)feature.getName()));
            gbb.setGridWidth(GridBagConstraints.REMAINDER);
            put(new JLabel(feature.getFeature().toString()));
        });
        return panel;
    }
    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
