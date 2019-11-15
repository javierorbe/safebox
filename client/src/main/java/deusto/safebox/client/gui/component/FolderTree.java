package deusto.safebox.client.gui.component;

import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.gui.model.FolderTreeModel;
import deusto.safebox.client.gui.renderer.FolderTreeCellRenderer;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

public class FolderTree extends JTree {

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
        });

    }

    public void updateModel() {
        // TODO: do this correctly and updateModel the model
        setModel(new FolderTreeModel());
    }
}
