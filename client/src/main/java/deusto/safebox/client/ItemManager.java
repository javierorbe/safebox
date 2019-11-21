package deusto.safebox.client;

import deusto.safebox.client.datamodel.BankAccount;
import deusto.safebox.client.datamodel.CreditCard;
import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.Identity;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.datamodel.Login;
import deusto.safebox.client.datamodel.Note;
import deusto.safebox.client.datamodel.WirelessRouter;
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

/**
 * Manages the items and provides an item change event.
 * Singleton.
 */
// TODO: change this to an static-method class
public enum ItemManager {
    INSTANCE;

    /** Item constructors with the parameter of their parent folder. */
    private static final Map<ItemType, Function<Folder, LeafItem>> ITEM_CONSTRUCTORS = new EnumMap<>(ItemType.class);

    // Register the item constructors.
    static {
        ITEM_CONSTRUCTORS.put(ItemType.LOGIN, Login::new);
        ITEM_CONSTRUCTORS.put(ItemType.NOTE, Note::new);
        ITEM_CONSTRUCTORS.put(ItemType.IDENTITY, Identity::new);
        ITEM_CONSTRUCTORS.put(ItemType.WIRELESS_ROUTER, WirelessRouter::new);
        ITEM_CONSTRUCTORS.put(ItemType.CREDIT_CARD, CreditCard::new);
        ITEM_CONSTRUCTORS.put(ItemType.BANK_ACCOUNT, BankAccount::new);
    }

    /** Folders at the top of the hierarchy. */
    private List<Folder> rootFolders = new ArrayList<>();
    private Map<ItemType, List<LeafItem>> itemMap = new EnumMap<>(ItemType.class);
    /** Listeners of folder/item changes. */
    private final Collection<Runnable> changeListeners = new HashSet<>();

    ItemManager() {
        // Initialize the lists in item map.
        Arrays.stream(ItemType.values())
                .forEach(type -> itemMap.put(type, new ArrayList<>()));
    }

    public List<Folder> getRootFolders() {
        return rootFolders;
    }

    /** Returns the list of items of the specified type. */
    public List<LeafItem> getItems(ItemType type) {
        return itemMap.get(type);
    }

    public void set(List<Folder> rootFolders, Map<ItemType, List<LeafItem>> itemMap) {
        this.rootFolders = rootFolders;
        this.itemMap = itemMap;
        Arrays.stream(ItemType.values()).forEach(type -> itemMap.computeIfAbsent(type, e -> new ArrayList<>()));
        fireChange();
    }

    public void addChangeListener(Runnable runnable) {
        changeListeners.add(runnable);
    }

    public void fireChange() {
        changeListeners.forEach(Runnable::run);
    }

    public void addItem(LeafItem item) {
        itemMap.get(item.getType()).add(item);
        item.getFolder().addItem(item);
        fireChange();
    }

    public void removeItem(LeafItem item) {
        itemMap.get(item.getType()).remove(item);
        fireChange();
    }

    public void addRootFolder(Folder folder) {
        rootFolders.add(folder);
        fireChange();
    }

    public void removeRootFolder(Folder folder) {
        rootFolders.remove(folder);
        fireChange();
    }

    /**
     * Returns all the folders and items as a collection of {@link AbstractItem}s.
     */
    public Collection<AbstractItem> getAll() {
        Collection<AbstractItem> items = new HashSet<>(getAllFolders());
        itemMap.forEach((type, itemList) -> items.addAll(itemList));
        return items;
    }

    /**
     * Returns a collection of all the folders (root folders and sub-folders).
     */
    private Collection<Folder> getAllFolders() {
        Collection<Folder> folders = new HashSet<>(rootFolders);
        rootFolders.forEach(rootFolder -> folders.addAll(rootFolder.getAllSubFolders()));
        return folders;
    }

    public void openNewItemDialog(JFrame owner, ItemType type, Folder folder) {
        LeafItem item = ITEM_CONSTRUCTORS.get(type).apply(folder);
        addItem(item);
        new EditItemDialog(owner, item);
    }
}
