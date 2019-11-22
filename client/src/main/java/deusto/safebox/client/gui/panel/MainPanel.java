package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.gui.component.DataTable;
import deusto.safebox.client.gui.component.FolderTree;
import deusto.safebox.client.gui.component.ItemTree;
import deusto.safebox.client.net.ErrorHandler;
import deusto.safebox.client.net.PacketHandler;
import deusto.safebox.common.net.packet.SuccessfulSaveDataPacket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

public class MainPanel extends JPanel {

    private final JScrollPane itemInfoPane = new JScrollPane();

    public MainPanel(JFrame owner) {
        super(new BorderLayout());

        DataTable table = new DataTable(owner, this::updateItemInfo);
        JScrollPane tableScrollPane = new JScrollPane(table);

        FolderTree folderTree = new FolderTree(owner, table);
        JScrollPane folderTreeScrollPane = new JScrollPane(folderTree);

        ItemTree itemTree = new ItemTree(table, this::updateItemInfo);
        JScrollPane itemTreeScrollPane = new JScrollPane(itemTree);

        JTabbedPane treeTabbedPane = new JTabbedPane();
        treeTabbedPane.setRequestFocusEnabled(false);
        treeTabbedPane.addTab("Folders", folderTreeScrollPane);
        treeTabbedPane.addTab("Categories", itemTreeScrollPane);
        treeTabbedPane.setPreferredSize(new Dimension(180, 0));

        ItemManager.addChangeListener(() -> {
            table.updateFolderModel();
            table.updateItemModels();
            folderTree.updateModel();
            itemTree.updateModel();
        });

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, itemInfoPane);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTabbedPane, rightSplitPane);

        add(mainSplitPane, BorderLayout.CENTER);

        PacketHandler.addListener(SuccessfulSaveDataPacket.class,
                ignored -> new ToastDialog("Data was successfully saved.", Color.GREEN, 2, this));
        ErrorHandler.addListener(ErrorType.SAVE_DATA_ERROR,
                () -> new ToastDialog("Error saving data.", Color.RED, 2, this));

        expandAll(folderTree);
        expandAll(itemTree);
    }

    private void updateItemInfo(LeafItem item) {
        itemInfoPane.setViewportView(new ItemInfoPanel(item));
    }

    /**
     * Expand all nodes in a tree
     * @param tree An JTree
     */
    private static void expandAll(JTree tree) {
        int i = 0;
        if (tree != null) {
            expandRecursively(tree, i);
        }
    }

    /**
     * Expand nodes in a tree recursively
     * If index > 0 expandRecursively will expand all nodes since of index until the final node
     * else if index = 0 expandRecursively will expand all tree's nodes
     * @param tree An JTree
     * @param index Index of the first node that is going to be expanded
     *              until the final node
     */
    private static void expandRecursively(JTree tree, int index) {
        if (index < tree.getRowCount()) {
            tree.expandRow(index);
            expandRecursively(tree, ++index);
        }
    }

    /**
     * Collapse all nodes in a tree
     * @param tree An JTree
     */
    private static void collapseAll(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++){
            tree.collapseRow(i);
        }
    }
}
