package deusto.safebox.client.gui.component;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.gui.model.FolderTableModel;
import deusto.safebox.client.gui.model.ItemTableModel;
import deusto.safebox.client.gui.panel.EditItemDialog;
import deusto.safebox.common.ItemType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

public class DataTable extends JTable {

    private final FolderTableModel folderTableModel = new FolderTableModel();
    private final Map<ItemType, ItemTableModel> itemTableModels = new EnumMap<>(ItemType.class);
    private ItemType currentItemType = null;
    private DataTableModel tableModel;

    public DataTable(JFrame owner, Consumer<LeafItem> itemSelectionEvent) {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                JTable table = (JTable) event.getSource();
                int row = table.rowAtPoint(event.getPoint());

                if (table.getSelectedRow() != -1) {
                    AbstractTableModel model;
                    LeafItem item;

                    if (tableModel == DataTableModel.FOLDER_MODEL) {
                        model = folderTableModel;
                        item = ((FolderTableModel) model).getItems().get(row);
                    } else { // DataTableModel.ITEM_MODEL
                        model = itemTableModels.get(currentItemType);
                        item = ItemManager.INSTANCE.getItems(currentItemType).get(row);
                    }

                    if (event.getClickCount() == 2) {
                        // TODO: show item modification dialog
                        EditItemDialog editDialog = new EditItemDialog(owner, item);
                        model.fireTableDataChanged();
                    } else {
                        itemSelectionEvent.accept(item);
                    }
                }
            }
        });

        Arrays.stream(ItemType.values())
                .forEach(type -> itemTableModels.put(type, new ItemTableModel(type)));
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
        updateFolderModel();
    }

    void selectItemModel(ItemType itemType) {
        tableModel = DataTableModel.ITEM_MODEL;
        currentItemType = itemType;
        setModel(itemTableModels.get(itemType));
    }

    public void updateFolderModel() {
        folderTableModel.fireTableDataChanged();
    }

    public void updateItemModels() {
        itemTableModels.values().forEach(ItemTableModel::fireTableDataChanged);
    }

    public enum DataTableModel {
        FOLDER_MODEL, ITEM_MODEL
    }
}
