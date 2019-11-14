package deusto.safebox.client.gui.model;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.common.ItemType;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ItemTreeModel implements TreeModel {

    private static final List<ItemType> ITEM_TYPE_ORDER = List.of(
            ItemType.LOGIN,
            ItemType.NOTE
    );

    private final Collection<TreeModelListener> listeners = new HashSet<>();

    @Override
    public Object getRoot() {
        return "Root";
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof ItemType) {
            return ItemManager.INSTANCE.getItems((ItemType) parent).get(index);
        } else {
            return ITEM_TYPE_ORDER.get(index);
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof ItemType) {
            return ItemManager.INSTANCE.getItems((ItemType) parent).size();
        } else {
            return ITEM_TYPE_ORDER.size();
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        if (node instanceof ItemType) {
            if (ItemManager.INSTANCE.getItems((ItemType) node).isEmpty()) {
                return true;
            }
        }
        return node instanceof LeafItem;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {}

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof ItemType && child instanceof LeafItem) {
            return ItemManager.INSTANCE.getItems((ItemType) parent).indexOf(child);
        } else if (child instanceof ItemType) {
            return ITEM_TYPE_ORDER.indexOf(child);
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
