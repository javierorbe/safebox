package deusto.safebox.client.gui.component;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.gui.model.FolderTableModel;
import deusto.safebox.client.gui.model.ItemTableModel;
import deusto.safebox.common.ItemType;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class DataTable extends JTable {

    private DataTableModel tableModel;
    private final FolderTableModel folderTableModel;

    public DataTable() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);

        folderTableModel = new FolderTableModel();

        setModel(folderTableModel);
    }

    FolderTableModel getFolderTableModel() {
        return folderTableModel;
    }

    DataTableModel getTableModel() {
        return tableModel;
    }

    void selectFolderModel() {
        tableModel = DataTableModel.FOLDER_MODEL;
        setModel(folderTableModel);
        folderTableModel.fireTableDataChanged();
    }

    void selectItemModel(ItemType itemType, List<LeafItem> items) {
        tableModel = DataTableModel.ITEM_MODEL;
        // TODO: to optimize this, map an ItemTableModel for each item type and instantiate each only once
        setModel(new ItemTableModel(itemType, items));
    }

    public enum DataTableModel {
        FOLDER_MODEL, ITEM_MODEL
    }
}
