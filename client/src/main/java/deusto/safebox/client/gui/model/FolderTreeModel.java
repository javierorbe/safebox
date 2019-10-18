package deusto.safebox.client.gui.model;

import deusto.safebox.client.datamodel.Folder;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FolderTreeModel implements TreeModel {

    private Collection<TreeModelListener> listeners = new HashSet<>();
    private List<Folder> folders;

    public FolderTreeModel(List<Folder> folders) {
        this.folders = folders;
    }

    @Override
    public Object getRoot() {
        return "Root";
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof Folder) {
            return ((Folder) parent).getSubFolders().get(index);
        } else {
            return folders.get(index);
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof Folder) {
            return ((Folder) parent).getSubFolderCount();
        } else {
            return folders.size();
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
                return folders.indexOf(child);
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
