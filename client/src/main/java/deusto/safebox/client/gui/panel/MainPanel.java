package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.net.packet.ErrorPacket.ErrorType;
import static deusto.safebox.common.util.GuiUtil.expandTree;
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
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class MainPanel extends JPanel {

    private final MainFrame mainFrame;
    private final FolderTree folderTree;
    private final ItemTree itemTree;
    private final JScrollPane itemInfoPane = new JScrollPane();

    public MainPanel(MainFrame mainFrame) {
        super(new BorderLayout());
        this.mainFrame = mainFrame;

        DataTable table = new DataTable(mainFrame, this::updateItemInfo);
        JScrollPane tableScrollPane = new JScrollPane(table);

        folderTree = new FolderTree(mainFrame, table);
        JScrollPane folderTreeScrollPane = new JScrollPane(folderTree);

        itemTree = new ItemTree(table, this::updateItemInfo);
        JScrollPane itemTreeScrollPane = new JScrollPane(itemTree);

        JTabbedPane treeTabbedPane = new JTabbedPane();
        treeTabbedPane.setRequestFocusEnabled(false);
        treeTabbedPane.addTab("Folders", folderTreeScrollPane);
        treeTabbedPane.addTab("Categories", itemTreeScrollPane);
        treeTabbedPane.setPreferredSize(new Dimension(180, 0));

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, itemInfoPane);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTabbedPane, rightSplitPane);
        add(mainSplitPane, BorderLayout.CENTER);

        registerListeners();
    }

    private void registerListeners() {
        ErrorHandler.addListener(ErrorType.SAVE_DATA_ERROR, () -> ToastDialog.showError(this, "Error saving data."));
        PacketHandler.INSTANCE.registerListener(SuccessfulSaveDataPacket.class,
                p -> ToastDialog.showInfo(this, "Data was successfully saved."));
        PacketHandler.INSTANCE.registerListener(RetrieveDataPacket.class,
                p -> ItemParser.fromItemData(p.getItems())
                        .thenAccept(pair -> {
                            ItemManager.initialize(pair.getLeft(), pair.getRight());
                            runSwing(() -> {
                                folderTree.buildFolderTree(ItemManager.getRootFolders());
                                itemTree.buildItemTree();
                                expandTree(folderTree);
                                expandTree(itemTree);
                                mainFrame.setCurrentPanel(MainFrame.PanelType.MAIN);
                            });
                        })
        );
    }

    /** Updates the item info panel with the properties of the provided item. */
    private void updateItemInfo(LeafItem item) {
        itemInfoPane.setViewportView(new ItemInfoPanel(item));
    }
}
