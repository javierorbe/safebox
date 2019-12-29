package deusto.safebox.client;

import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.datamodel.Login;
import deusto.safebox.client.datamodel.Note;
import deusto.safebox.client.gui.panel.EditItemDialog;
import deusto.safebox.common.AbstractItem;
import deusto.safebox.common.ItemType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.JFrame;

/** Manages the items and provides an item change event. */
public class ItemManager {

    /** Folders at the top of the hierarchy. */
    private static List<Folder> rootFolders = new ArrayList<>();
    /** Map of items grouped by their type. */
    private static Map<ItemType, List<LeafItem>> itemMap = new EnumMap<>(ItemType.class);

    private static Runnable itemChangeListener;

    /** Item constructors with the parameter of their parent folder. */
    private static final Map<ItemType, Function<Folder, LeafItem>> ITEM_CONSTRUCTORS = new EnumMap<>(ItemType.class);

    static {
        // Initialize the lists in item map.
        Arrays.stream(ItemType.values())
                .forEach(type -> itemMap.put(type, new ArrayList<>()));

        // Register the item constructors.
        ITEM_CONSTRUCTORS.put(ItemType.LOGIN, Login::new);
        ITEM_CONSTRUCTORS.put(ItemType.NOTE, Note::new);
        // TODO: uncomment when implemented
        // ITEM_CONSTRUCTORS.put(ItemType.IDENTITY, Identity::new);
        // ITEM_CONSTRUCTORS.put(ItemType.WIRELESS_ROUTER, WirelessRouter::new);
        // ITEM_CONSTRUCTORS.put(ItemType.CREDIT_CARD, CreditCard::new);
        // ITEM_CONSTRUCTORS.put(ItemType.BANK_ACCOUNT, BankAccount::new);
    }

    public static List<Folder> getRootFolders() {
        return rootFolders;
    }

    /** Returns the list of items of the specified type. */
    public static List<LeafItem> getItems(ItemType type) {
        return itemMap.get(type);
    }

    public static void setItemChangeListener(Runnable listener) {
        itemChangeListener = listener;
    }

    public static void initialize(List<Folder> newRootFolders, Map<ItemType, List<LeafItem>> newItemMap) {
        rootFolders = newRootFolders;
        itemMap = newItemMap;

        Arrays.stream(ItemType.values())
                .forEach(type -> itemMap.computeIfAbsent(type, t -> new ArrayList<>()));

        itemChangeListener.run();
    }

    public static void addItem(LeafItem item) {
        itemMap.get(item.getType()).add(item);
        item.getFolder().addItem(item);
        itemChangeListener.run();
    }

    public static void removeItem(LeafItem item) {
        itemMap.get(item.getType()).remove(item);
        itemChangeListener.run();
    }

    public static void addRootFolder(Folder folder) {
        rootFolders.add(folder);
    }

    /** Removes a folder and all its subfolders, including the items. */
    public static void removeFolder(Folder folder) {
        if (folder.isRootFolder()) {
            rootFolders.remove(folder);
        } else {
            folder.getFolder().removeSubFolder(folder);
        }
        removeAllSubFolders(folder);
        itemChangeListener.run();
    }

    /** Removes all the items from the folder and its subfolders. */
    private static void removeAllSubFolders(Folder folder) {
        folder.getItems().forEach(ItemManager::removeItem);
        folder.getSubFolders().forEach(ItemManager::removeAllSubFolders);
    }

    /**
     * Returns all the folders and items as a collection of {@link AbstractItem}s.
     */
    public static Collection<AbstractItem> getAll() {
        Collection<AbstractItem> items = new HashSet<>(getAllFolders());
        itemMap.forEach((type, itemList) -> items.addAll(itemList));
        return items;
    }

    /** Returns a collection of all the folders (root folders and sub-folders). */
    private static Collection<Folder> getAllFolders() {
        Collection<Folder> folders = new HashSet<>(rootFolders);
        rootFolders.forEach(rootFolder -> folders.addAll(rootFolder.getAllSubFolders()));
        return folders;
    }

    public static void showNewItemDialog(JFrame owner, ItemType type, Folder folder) {
        LeafItem item = ITEM_CONSTRUCTORS.get(type).apply(folder);
        new EditItemDialog(owner, item);
        addItem(item);
    }

    private ItemManager() {
        throw new AssertionError();
    }
}
