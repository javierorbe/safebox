package deusto.safebox.client.gui.model;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.Folder;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class FolderTreeModel extends DefaultTreeModel {

    private final Collection<TreeModelListener> listeners = new HashSet<>();

    public FolderTreeModel() {
        super(new DefaultMutableTreeNode());
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof Folder) {
            return ((Folder) parent).getSubFolders().get(index);
        } else {
            return ItemManager.INSTANCE.getRootFolders().get(index);
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof Folder) {
            return ((Folder) parent).getSubFolderCount();
        } else {
            return ItemManager.INSTANCE.getRootFolders().size();
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        if (node instanceof Folder) {
            return ((Folder) node).isLeafFolder();
        } else {
            return false;
        }
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {}

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (child instanceof Folder) {
            if (parent instanceof Folder) {
                return ((Folder) parent).getSubFolders().indexOf(child);
            } else {
                return ItemManager.INSTANCE.getRootFolders().indexOf(child);
            }
        } else {
            return 0;
        }
    }

    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}
