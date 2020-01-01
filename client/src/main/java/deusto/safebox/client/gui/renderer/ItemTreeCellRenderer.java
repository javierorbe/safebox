package deusto.safebox.client.gui.renderer;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.ItemType;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ItemTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.getUserObject() instanceof ItemType) {
            ItemType nodeType = (ItemType) node.getUserObject();
            setText(nodeType.getName());
            // TODO: set different icons for each item type
            setIcon(IconType.FOLDER.getAsIcon());
        } else if (node.getUserObject() instanceof LeafItem) {
            LeafItem nodeItem = (LeafItem) node.getUserObject();
            setText(nodeItem.getTitle());
            // TODO: customize the icon of each item
            setIcon(IconType.NEW_FILE_16.getAsIcon());
        }

        return this;
    }
}
