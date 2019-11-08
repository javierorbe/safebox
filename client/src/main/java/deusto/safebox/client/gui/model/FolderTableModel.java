package deusto.safebox.client.gui.model;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.common.util.Constants;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class FolderTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {
        "Name",
        "Type",
        "Created",
        "Last modified"
    };

    private List<LeafItem> items = new ArrayList<>();

    public FolderTableModel() {}

    public void setItems(List<LeafItem> items) {
        this.items = items;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        if (column < COLUMN_NAMES.length) {
            return COLUMN_NAMES[column];
        } else {
            return super.getColumnName(column);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LeafItem item = items.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getName();
            case 1:
                return item.getType().getName();
            case 2:
                return Constants.DATE_TIME_FORMATTER.format(item.getCreated());
            case 3:
                return Constants.DATE_TIME_FORMATTER.format(item.getLastModified());
            default:
                return "";
        }
    }
}
