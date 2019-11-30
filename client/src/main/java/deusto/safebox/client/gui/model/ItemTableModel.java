package deusto.safebox.client.gui.model;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.BankAccount;
import deusto.safebox.client.datamodel.CreditCard;
import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.Identity;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.datamodel.Login;
import deusto.safebox.client.datamodel.Note;
import deusto.safebox.client.datamodel.WirelessRouter;
import deusto.safebox.client.datamodel.property.ItemProperty;
import deusto.safebox.client.datamodel.property.LongStringProperty;
import deusto.safebox.client.datamodel.property.PasswordProperty;
import deusto.safebox.common.ItemType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ItemTableModel extends AbstractTableModel {

    private static final Map<ItemType, List<String>> ITEM_PROPERTY_NAMES = new EnumMap<>(ItemType.class);
    private static final Map<ItemType, List<Integer>> ITEM_PROPERTY_INDICES = new EnumMap<>(ItemType.class);

    static {
        for (ItemType type : ItemType.values()) {
            ITEM_PROPERTY_NAMES.put(type, new ArrayList<>());
            ITEM_PROPERTY_INDICES.put(type, new ArrayList<>());
        }

        Collection<Class<? extends ItemProperty>> ignoredProperties = new HashSet<>();
        ignoredProperties.add(PasswordProperty.class);
        ignoredProperties.add(LongStringProperty.class);

        Map<ItemType, Function<Folder, LeafItem>> itemConstructors = new EnumMap<>(ItemType.class);
        itemConstructors.put(ItemType.LOGIN, Login::new);
        itemConstructors.put(ItemType.NOTE, Note::new);
        itemConstructors.put(ItemType.BANK_ACCOUNT, BankAccount::new);
        itemConstructors.put(ItemType.CREDIT_CARD, CreditCard::new);
        itemConstructors.put(ItemType.IDENTITY, Identity::new);
        itemConstructors.put(ItemType.WIRELESS_ROUTER, WirelessRouter::new);
        // TODO: add the remaining constructors

        itemConstructors.forEach((type, constructor) -> {
            LeafItem item = constructor.apply(null);
            for (int i = 0; i < item.getProperties().size(); i++) {
                ItemProperty prop = item.getProperty(i);
                if (!ignoredProperties.contains(prop.getClass())) {
                    ITEM_PROPERTY_NAMES.get(type).add(prop.getDisplayName());
                    ITEM_PROPERTY_INDICES.get(type).add(i);
                }
            }
        });
    }

    private final ItemType itemType;

    public ItemTableModel(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getRowCount() {
        return ItemManager.getItems(itemType).size();
    }

    @Override
    public int getColumnCount() {
        return ITEM_PROPERTY_NAMES.get(itemType).size();
    }

    @Override
    public String getColumnName(int column) {
        return ITEM_PROPERTY_NAMES.get(itemType).get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LeafItem item = ItemManager.getItems(itemType).get(rowIndex);
        return item.getProperty(ITEM_PROPERTY_INDICES.get(item.getType()).get(columnIndex)).toString();
    }
}
