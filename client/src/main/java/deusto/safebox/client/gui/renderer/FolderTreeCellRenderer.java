package deusto.safebox.client.gui.renderer;

import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.util.IconManager;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FolderTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value instanceof Folder) {
            setText(((Folder) value).getItemName());
            setIcon(IconManager.getAsIcon(IconManager.IconType.FOLDER));
        }

        return this;
    }
}
