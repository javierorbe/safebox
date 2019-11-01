package deusto.safebox.client.gui.model;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.common.ItemType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ItemTableModel extends AbstractTableModel {

    private static final Map<ItemType, List<String>> ITEM_PROPERTIES = new EnumMap<>(ItemType.class);

    static {
        ITEM_PROPERTIES.put(ItemType.LOGIN, List.of(
                "Title",
                "Username",
                "Password",
                "URL",
                "Created",
                "Last Modified"
        ));
        ITEM_PROPERTIES.put(ItemType.NOTE, List.of(
                "Title",
                "Content",
                "Created",
                "Last Modified"
        ));
    }

    private final ItemType itemType;
    private final List<LeafItem> items;

    public ItemTableModel(ItemType itemType, List<LeafItem> items) {
        this.itemType = itemType;
        this.items = items;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return ITEM_PROPERTIES.get(itemType).size();
    }

    @Override
    public String getColumnName(int column) {
        return ITEM_PROPERTIES.get(itemType).get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return items.get(rowIndex).getProperty(columnIndex);
    }
}
