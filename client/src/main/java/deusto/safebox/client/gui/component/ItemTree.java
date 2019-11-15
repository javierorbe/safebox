package deusto.safebox.client.gui.component;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.gui.model.ItemTreeModel;
import deusto.safebox.client.gui.renderer.ItemTreeCellRenderer;
import deusto.safebox.common.ItemType;
import java.util.function.Consumer;
import javax.swing.JTree;

public class ItemTree extends JTree {

    public ItemTree(DataTable table, Consumer<LeafItem> itemSelectionEvent) {
        super(new ItemTreeModel());

        setRootVisible(false);
        setShowsRootHandles(true);
        setCellRenderer(new ItemTreeCellRenderer());

        addTreeSelectionListener(event -> {
            Object object = event.getPath().getLastPathComponent();
            if (object instanceof ItemType) {
                table.selectItemModel((ItemType) object);
            } else if (object instanceof LeafItem) {
                itemSelectionEvent.accept((LeafItem) object);
            }
        });
    }

    public void updateModel() {
        // TODO: do this correctly and updateModel the model
        setModel(new ItemTreeModel());
    }
}
