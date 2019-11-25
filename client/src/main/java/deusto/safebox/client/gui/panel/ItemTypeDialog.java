package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static java.util.stream.Collectors.toCollection;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.SimpleButton;
import deusto.safebox.common.util.GuiUtil;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ItemTypeDialog extends JDialog {

    private final GridBagBuilder gbb = new GridBagBuilder();

    public ItemTypeDialog(JFrame owner, Folder folder) {
        super(owner, "Item type selection", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(480, 240));
        setLayout(new GridBagLayout());

        Vector<ItemType> types = Arrays.stream(ItemType.values())
                .collect(toCollection(Vector::new));
        types.remove(ItemType.FOLDER);

        JComboBox<ItemType> itemTypeSelector = new JComboBox<>(types);

        JButton okBtn = new SimpleButton("OK", () -> {
            ItemType selectedType = (ItemType) itemTypeSelector.getSelectedItem();
            dispose();
            ItemManager.openNewItemDialog(owner, Objects.requireNonNull(selectedType), folder);
        });

        gbb.setInsets(4, 4, 4, 4)
                .setAnchor(Anchor.WEST);
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(itemTypeSelector);
        gbb.setAnchor(Anchor.PAGE_END);
        put(okBtn);

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
