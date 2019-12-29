package deusto.safebox.client.gui.component;

import static deusto.safebox.common.util.GuiUtil.runSwing;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.gui.menu.ItemPopupMenu;
import deusto.safebox.client.gui.panel.ItemTypeDialog;
import deusto.safebox.client.util.IconType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class FolderTree extends JTree {

    private final JFrame mainFrame;
    private final DataTable table;
    private final DefaultTreeModel model;
    private final DefaultMutableTreeNode root;

    public FolderTree(JFrame mainFrame, DataTable table) {
        this.mainFrame = mainFrame;
        this.table = table;

        root = new DefaultMutableTreeNode();
        model = new FolderTreeModel(root);

        setModel(model);
        setRootVisible(false);
        setShowsRootHandles(true);
        setEditable(true);

        FolderTreeCellRenderer renderer = new FolderTreeCellRenderer();
        setCellRenderer(renderer);
        setCellEditor(new FolderCellEditor(this, renderer));

        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(e -> handleTreeSelection());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                // Clear selection if clicked outside a node
                if (getRowForLocation(event.getX(), event.getY()) == -1) {
                    clearSelection();
                }

                if (SwingUtilities.isRightMouseButton(event)) {
                    getComponentPopupMenu().show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });

        setComponentPopupMenu(new ItemPopupMenu(this::onNewItem, this::onNewFolder, this::onDeleteFolder));
    }

    public void buildFolderTree(List<Folder> rootFolders) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        for (Folder rootFolder : rootFolders) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(rootFolder);
            root.add(node);
            buildSubFolders(node);
        }
        model.reload();
    }

    private void buildSubFolders(DefaultMutableTreeNode parent) {
        Folder folder = (Folder) parent.getUserObject();
        for (Folder subFolder : folder.getSubFolders()) {
            DefaultMutableTreeNode subFolderNode = new DefaultMutableTreeNode(subFolder);
            parent.add(subFolderNode);
            if (subFolder.hasSubFolders()) {
                buildSubFolders(subFolderNode);
            }
        }
    }

    private void handleTreeSelection() {
        if (table.getTableModel() != DataTable.DataTableModel.FOLDER_MODEL) {
            table.selectFolderModel();
        }

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (selectedNode != null) {
            Folder selectedFolder = (Folder) selectedNode.getUserObject();
            table.getFolderTableModel().setFolder(selectedFolder);
        }
    }

    private void onNewItem() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (node != null) {
            Folder folder = (Folder) node.getUserObject();
            new ItemTypeDialog(mainFrame, Objects.requireNonNull(folder));
            table.updateFolderModel();
        }
    }

    private void onNewFolder() {
        Folder folder = new Folder("Folder (" + (root.getChildCount() + 1) + ")");
        DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(folder);
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();

        // If there is no selected folder, then a root folder is created.
        if (selectedNode == null) {
            root.add(folderNode);
            ItemManager.addRootFolder(folder);
        } else {
            selectedNode.add(folderNode);
            Folder parent = (Folder) selectedNode.getUserObject();
            parent.addSubFolder(folder);
        }

        model.reload();
    }

    private void onDeleteFolder() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (selectedNode != null) {
            Folder selectedFolder = (Folder) selectedNode.getUserObject();

            selectedNode.removeAllChildren();
            selectedNode.removeFromParent();
            ItemManager.removeFolder(selectedFolder);

            model.reload();
        }
    }

    @SuppressWarnings("serial")
    private static class FolderTreeModel extends DefaultTreeModel {

        public FolderTreeModel(DefaultMutableTreeNode root) {
            super(root);
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (node.getUserObject() instanceof Folder && newValue instanceof String) {
                Folder folder = (Folder) node.getUserObject();
                folder.setTitle((String) newValue);
            }
            nodeChanged(node);
        }
    }

    public static class FolderTreeCellRenderer extends DefaultTreeCellRenderer {

        public FolderTreeCellRenderer() {
            Icon icon = IconType.FOLDER.getAsIcon();
            setIcon(icon);
            setOpenIcon(icon);
            setDisabledIcon(icon);
            setLeafIcon(icon);
            setClosedIcon(icon);
        }
    }

    private static class FolderCellEditor extends DefaultTreeCellEditor {

        public FolderCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
            super(tree, renderer);
        }

        @Override
        public boolean isCellEditable(EventObject event) {
            if (event instanceof MouseEvent) {
                return ((MouseEvent) event).getClickCount() >= 2;
            }
            return super.isCellEditable(event);
        }
    }
}
