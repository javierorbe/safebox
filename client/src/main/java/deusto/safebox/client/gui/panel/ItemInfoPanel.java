package deusto.safebox.client.gui.panel;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ItemInfoPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ItemInfoPanel(LeafItem item) {
        super(new GridBagLayout());

        gbb.setInsets(6, 6, 6, 6);
        gbb.setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);

        put(panelByItemType(item));
    }

    private JPanel panelByItemType(LeafItem item) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        item.getFeatures().forEach(feature -> {
            gbb.setGridWidthAndWeightX(1, 1);
            put(new JLabel(feature.getName()));
            gbb.setGridWidth(GridBagConstraints.REMAINDER);
            if (feature.getFeature() instanceof LocalDateTime) {
                put(new JLabel(((LocalDateTime) feature.getFeature()).format(formatter)));
            } else {
                put(new JLabel(feature.getFeature().toString()));
            }
        });
        return panel;
    }
    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}