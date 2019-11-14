package deusto.safebox.client.gui.model;

import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.util.Pair;
import deusto.safebox.common.util.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class FolderTableModel extends AbstractTableModel {

    private static final List<Pair<String, Function<LeafItem, Object>>> COLUMNS = List.of(
            new Pair<>("Title", LeafItem::getName),
            new Pair<>("Type", LeafItem::getType),
            new Pair<>("Created", item -> Constants.DATE_TIME_FORMATTER.format(item.getCreated())),
            new Pair<>("Last Modified", item -> Constants.DATE_TIME_FORMATTER.format(item.getLastModified()))
    );

    private List<LeafItem> items = new ArrayList<>();

    public void setFolder(Folder folder) {
        items = folder.getItems();
        fireTableDataChanged();
    }

    public List<LeafItem> getItems() {
        return items;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.size();
    }

    @Override
    public String getColumnName(int column) {
        if (column >= COLUMNS.size()) {
            throw new IllegalArgumentException("Invalid column (" + column + ")");
        }

        return COLUMNS.get(column).getLeft();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex >= COLUMNS.size()) {
            throw new IllegalArgumentException("Invalid columnIndex (" + columnIndex + ")");
        }
        return COLUMNS.get(columnIndex).getRight().apply(items.get(rowIndex));
    }
}
