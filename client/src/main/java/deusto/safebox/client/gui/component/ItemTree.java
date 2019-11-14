package deusto.safebox.client.gui.component;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.gui.model.ItemTreeModel;
import deusto.safebox.client.gui.renderer.ItemTreeCellRenderer;
import deusto.safebox.common.ItemType;
import java.util.List;
import java.util.Map;
import javax.swing.JTree;

public class ItemTree extends JTree {

    public ItemTree(Map<ItemType, List<LeafItem>> items, DataTable table) {
        super(new ItemTreeModel());

        setRootVisible(false);
        setShowsRootHandles(true);
        setCellRenderer(new ItemTreeCellRenderer());

        addTreeSelectionListener((e) -> {
            Object o = e.getPath().getLastPathComponent();
            if (o instanceof ItemType) {
                table.selectItemModel((ItemType) o);
            } else if (o instanceof LeafItem) {
                // TODO: show item info on display box
            }
        });
    }
}
