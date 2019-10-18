package deusto.safebox.client.gui.model;

import deusto.safebox.client.datamodel.Item;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.common.ItemType;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ItemTreeModel implements TreeModel {

    private static final List<ItemType> ITEM_TYPE_ORDER = List.of(
            ItemType.LOGIN,
            ItemType.NOTE
    );

    private Collection<TreeModelListener> listeners = new HashSet<>();
    private Map<ItemType, List<LeafItem>> items;

    public ItemTreeModel(Map<ItemType, List<LeafItem>> items) {
        this.items = items;
    }

    @Override
    public Object getRoot() {
        return "Root";
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof ItemType) {
            return items.get(parent).get(index);
        } else {
            return ITEM_TYPE_ORDER.get(index);
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof ItemType) {
            return items.get(parent).size();
        } else {
            return ITEM_TYPE_ORDER.size();
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        if (node instanceof ItemType) {
            if (items.get(node).isEmpty()) {
                return true;
            }
        }
        return node instanceof Item;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {}

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof ItemType && child instanceof Item) {
            return items.get(parent).indexOf(child);
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