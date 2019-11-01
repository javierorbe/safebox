package deusto.safebox.client.gui.component;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.gui.model.FolderTableModel;
import deusto.safebox.client.gui.model.ItemTableModel;
import deusto.safebox.common.ItemType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class DataTable extends JTable {

    private final FolderTableModel folderTableModel;
    private final Map<ItemType, ItemTableModel> itemTableModels = new EnumMap<>(ItemType.class);
    private DataTableModel tableModel;

    public DataTable(Map<ItemType, List<LeafItem>> items) {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);

        folderTableModel = new FolderTableModel();
        items.keySet().forEach(type -> itemTableModels.put(type, new ItemTableModel(type, items.get(type))));

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

    void selectItemModel(ItemType itemType) {
        tableModel = DataTableModel.ITEM_MODEL;
        setModel(itemTableModels.get(itemType));
    }

    public enum DataTableModel {
        FOLDER_MODEL, ITEM_MODEL
    }
}
