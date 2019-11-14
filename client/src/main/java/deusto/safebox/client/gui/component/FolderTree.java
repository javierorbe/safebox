package deusto.safebox.client.gui.component;

import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.gui.model.FolderTreeModel;
import deusto.safebox.client.gui.renderer.FolderTreeCellRenderer;
import java.util.List;
import javax.swing.JTree;

public class FolderTree extends JTree {

    public FolderTree(List<Folder> folders, DataTable table) {
        super(new FolderTreeModel(folders));

        setRootVisible(false);
        setShowsRootHandles(true);
        setCellRenderer(new FolderTreeCellRenderer());

        addTreeSelectionListener((e) -> {
            if (table.getTableModel() != DataTable.DataTableModel.FOLDER_MODEL) {
                table.selectFolderModel();
            }

            Folder folder = (Folder) e.getPath().getLastPathComponent();
            table.getFolderTableModel().setFolder(folder);
        });
    }
}
