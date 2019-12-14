package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType;
import static deusto.safebox.common.util.GuiUtil.runSwing;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.ItemParser;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.gui.MainFrame;
import deusto.safebox.client.gui.component.DataTable;
import deusto.safebox.client.gui.component.FolderTree;
import deusto.safebox.client.gui.component.ItemTree;
import deusto.safebox.client.net.ErrorHandler;
import deusto.safebox.client.net.PacketHandler;
import deusto.safebox.common.net.packet.RetrieveDataPacket;
import deusto.safebox.common.net.packet.SuccessfulSaveDataPacket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

public class MainPanel extends JPanel {

    private final JScrollPane itemInfoPane = new JScrollPane();

    public MainPanel(MainFrame owner) {
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
            itemTree.updateModel();
        });

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, itemInfoPane);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTabbedPane, rightSplitPane);

        add(mainSplitPane, BorderLayout.CENTER);

        ErrorHandler.addListener(ErrorType.SAVE_DATA_ERROR,
                () -> new ToastDialog("Error saving data.", Color.RED, 2, this));
        PacketHandler.INSTANCE.registerListener(SuccessfulSaveDataPacket.class, e ->
                new ToastDialog("Data was successfully saved.", Color.GREEN, 2, MainPanel.this));
        PacketHandler.INSTANCE.registerListener(RetrieveDataPacket.class, packet ->
                ItemParser.fromItemData(packet.getItems())
                        .thenAccept(pair -> ItemManager.set(pair.getLeft(), pair.getRight()))
                        .thenRun(() -> {
                            folderTree.build(ItemManager.getRootFolders());
                            runSwing(() -> {
                                expandAll(folderTree);
                                expandAll(itemTree);
                                owner.setCurrentPanel(MainFrame.PanelType.MAIN);
                            });
                        })
        );
    }

    /** Updates the item info panel with the properties of the provided item. */
    private void updateItemInfo(LeafItem item) {
        itemInfoPane.setViewportView(new ItemInfoPanel(item));
    }

    /**
     * Expands all rows in a {@link JTree}.
     *
     * @param tree the tree
     */
    private static void expandAll(JTree tree) {
        expandRecursively(tree, 0);
    }

    /**
     * Expands the rows in a {@link JTree}, starting from the {@code index} row to the last row.
     *
     * @param tree the tree
     * @param index the index of the first row to expand
     */
    private static void expandRecursively(JTree tree, int index) {
        if (index < tree.getRowCount()) {
            tree.expandRow(index);
            expandRecursively(tree, ++index);
        }
    }

    /**
     * Collapses all rows in a {@link JTree}.
     *
     * @param tree the tree
     */
    private static void collapseAll(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.collapseRow(i);
        }
    }
}
