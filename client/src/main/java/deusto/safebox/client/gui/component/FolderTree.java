package deusto.safebox.client.gui.component;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.gui.menu.ItemPopupMenu;
import deusto.safebox.client.gui.model.FolderTreeModel;
import deusto.safebox.client.gui.panel.ItemTypeDialog;
import deusto.safebox.client.gui.renderer.FolderTreeCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class FolderTree extends JTree {

    private Folder selectedFolder;

    public FolderTree(JFrame owner, DataTable table) {
        super(new FolderTreeModel());

        setRootVisible(false);
        setShowsRootHandles(true);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setCellRenderer(new FolderTreeCellRenderer());

        addTreeSelectionListener((e) -> {
            if (table.getTableModel() != DataTable.DataTableModel.FOLDER_MODEL) {
                table.selectFolderModel();
            }

            Folder folder = (Folder) e.getPath().getLastPathComponent();
            table.getFolderTableModel().setFolder(folder);
            selectedFolder = folder;
        });

        // TODO: fix folder selection

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    int row = getRowForLocation(event.getX(), event.getY());
                    TreePath selectionPath = getPathForLocation(event.getX(), event.getY());
                    if (row != -1 && selectionPath != null) {
                        selectedFolder = (Folder) selectionPath.getLastPathComponent();
                        getComponentPopupMenu().show(event.getComponent(), event.getX(), event.getY());
                    }
                }
            }
        });

        setComponentPopupMenu(new ItemPopupMenu(
                () -> {
                    new ItemTypeDialog(owner, Objects.requireNonNull(selectedFolder));
                    table.updateFolderModel();
                },
                () -> {},
                () -> {},
                () -> {
                    // TEMP
                    Random random = ThreadLocalRandom.current();
                    Folder folder = new Folder("Folder" + random.nextInt(100));
                    if (selectedFolder == null) {
                        ItemManager.INSTANCE.addRootFolder(folder);
                    } else {
                        selectedFolder.addSubFolder(folder);
                        ItemManager.INSTANCE.fireChange();
                    }
                },
                () -> {},
                () -> {}
        ));
    }

    public void updateModel() {
        // TODO: do this correctly and updateModel the model
        setModel(new FolderTreeModel());
    }
}
