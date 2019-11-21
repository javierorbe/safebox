package deusto.safebox.client.gui.panel;

import deusto.safebox.client.datamodel.ItemProperty;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.common.gui.GridBagBuilder;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

public class EditItemDialog extends JDialog {

    private GridBagBuilder gbb = new GridBagBuilder();
    private ArrayList<JComponent> itemComponents = new ArrayList<>();

    public EditItemDialog(JFrame owner, LeafItem item) {

        super(owner, "Create/Edit item", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        gbb.setInsets(6, 6, 6, 6);
        gbb.setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);

        updateFeaturesComponents(item);
        pack();
        setVisible(true);
    }

    private void updateFeaturesComponents(LeafItem item) {
        int i = 0;
        for (ItemProperty feature : item.getFeatures()) {
            itemComponents.add(feature.getComponent());
            gbb.setGridWidthAndWeightX(1, 1);
            put(new JLabel(feature.getName()));
            gbb.setGridWidth(GridBagConstraints.REMAINDER);
            put(itemComponents.get(i));
            i++;
        }
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
