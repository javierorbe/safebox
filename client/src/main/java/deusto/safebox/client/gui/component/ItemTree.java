package deusto.safebox.client.gui.component;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.gui.menu.ItemPopupMenu;
import deusto.safebox.client.gui.renderer.ItemTreeCellRenderer;
import deusto.safebox.common.ItemType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ItemTree extends JTree {

    private final DefaultTreeModel model;
    private final DefaultMutableTreeNode root;
    private DataTable table;
    private final Map<ItemType, DefaultMutableTreeNode> ITEM_TYPES_NODES = new HashMap<>();
    private final Map<LeafItem, DefaultMutableTreeNode> ITEM_NODES = new HashMap<>();
    private final Consumer<LeafItem> itemSelectionEvent;

    public ItemTree(DataTable table, Consumer<LeafItem> itemSelectionEvent) {
        this.table = table;
        this.itemSelectionEvent = itemSelectionEvent;

        root = new DefaultMutableTreeNode();
        model = new ItemTreeModel(root);

        setModel(model);
        setRootVisible(false);
        setShowsRootHandles(true);
        setCellRenderer(new ItemTreeCellRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        addTreeSelectionListener(e -> handleTreeSelection());
        setComponentPopupMenu(new ItemPopupMenu(this::removeItem));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                // Clear selection if clicked outside a node
                if (getRowForLocation(event.getX(), event.getY()) == -1) {
                    clearSelection();
                }
            }
        });

        ItemManager.setItemAddedEvent(this::addItem);
        ItemManager.setItemRemovedEvent(this::removeItem);
    }

    public void buildItemTree() {
        for (ItemType type : ItemType.values()) {
            if (type != ItemType.FOLDER) {
                DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(type);
                root.add(typeNode);
                ITEM_TYPES_NODES.put(type, typeNode);
                for (LeafItem item : ItemManager.getItems(type)) {
                    addItem(item);
                }
            }
        }

        model.reload();
    }

    private void handleTreeSelection() {
        if (getLastSelectedPathComponent() == null) {
            return;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        Object object = node.getUserObject();

        if (object instanceof ItemType) {
            table.selectItemModel((ItemType) object);
        } else if (object instanceof LeafItem) {
            table.selectItemModel(((LeafItem) object).getType());
            itemSelectionEvent.accept((LeafItem) object);
        }
    }

    private void addItem(LeafItem item) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(item);
        ITEM_TYPES_NODES.get(item.getType()).add(node);
        ITEM_NODES.put(item, node);
        model.reload();
    }

    private void removeItem(LeafItem item) {
        DefaultMutableTreeNode parent = ITEM_TYPES_NODES.get(item.getType());
        parent.remove(ITEM_NODES.get(item));
        ITEM_NODES.remove(item);
        table.updateItemModels();
        model.reload();
    }

    private void removeItem() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();

        if (selectedNode != null && selectedNode.getUserObject() instanceof LeafItem) {
            ItemManager.removeItemAndFromParent((LeafItem) selectedNode.getUserObject());
            table.updateItemModels();
            model.reload();
        }
    }

    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    @SuppressWarnings("serial")
    private static class ItemTreeModel extends DefaultTreeModel {

        public ItemTreeModel(DefaultMutableTreeNode root) {
            super(root);
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (node.getUserObject() instanceof LeafItem && newValue instanceof String) {
                LeafItem item = (LeafItem) node.getUserObject();
                item.setTitle((String) newValue);
            }
            nodeChanged(node);
        }
    }
}