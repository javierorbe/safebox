package deusto.safebox.client.datamodel;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.google.gson.JsonObject;
import deusto.safebox.client.security.ClientSecurity;
import deusto.safebox.client.util.Pair;
import deusto.safebox.client.util.TriFunction;
import deusto.safebox.common.AbstractItem;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.util.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemParser {

    private static final Map<ItemType, TriFunction<ItemData, Folder, JsonObject, LeafItem>> ITEM_BUILDERS
            = new EnumMap<>(ItemType.class);

    static {
        ITEM_BUILDERS.put(ItemType.LOGIN, Login::build);
        ITEM_BUILDERS.put(ItemType.NOTE, Note::build);
        ITEM_BUILDERS.put(ItemType.IDENTITY, Identity::build);
        ITEM_BUILDERS.put(ItemType.WIRELESS_ROUTER, WirelessRouter::build);
        ITEM_BUILDERS.put(ItemType.CREDIT_CARD, CreditCard::build);
        ITEM_BUILDERS.put(ItemType.BANK_ACCOUNT, BankAccount::build);
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(3);

    /**
     * Transforms a collection of {@link AbstractItem} into a collection of {@link ItemData}.
     *
     * @param items the {@link AbstractItem} collection.
     * @return the {@link ItemData} collection.
     */
    public static CompletableFuture<Collection<ItemData>> toItemData(Collection<AbstractItem> items) {
        return CompletableFuture.supplyAsync(
                () -> items.parallelStream()
                        .map(ItemData::new)
                        .collect(toSet()),
                EXECUTOR_SERVICE
        );
    }

    /**
     * Transforms a collection of {@link ItemData}
     * into a list of root folders and a map of {@link LeafItem}s grouped by type.
     *
     * @param itemDataCollection the {@link ItemData} collection.
     * @return a {@link Pair} containing the root folder list and the item map.
     */
    public static CompletableFuture<Pair<List<Folder>, Map<ItemType, List<LeafItem>>>>
    fromItemData(Collection<ItemData> itemDataCollection) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Pair<Map<Folder, UUID>, Map<UUID, Folder>> pair = buildFolderMaps(itemDataCollection).get();
                CompletableFuture<List<Folder>> f1 = getRootFolders(pair.getRight(), pair.getLeft());
                CompletableFuture<Map<ItemType, List<LeafItem>>> f2
                        = getLeafItemMap(itemDataCollection, pair.getRight());
                CompletableFuture.allOf(f1, f2).get();
                return new Pair<>(f1.get(), f2.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error parsing items.", e);
            }
        }, EXECUTOR_SERVICE);
    }

    private static CompletableFuture<Pair<Map<Folder, UUID>, Map<UUID, Folder>>>
    buildFolderMaps(Collection<ItemData> collection) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Folder, UUID> parentFolderMap = new HashMap<>();
            Map<UUID, Folder> folderMap = collection.parallelStream()
                    .filter(item -> item.getType() == ItemType.FOLDER)
                    .collect(toMap(ItemData::getId, itemData -> {
                        String decryptedJson;
                        try {
                            decryptedJson = ClientSecurity.decrypt(itemData.getEncryptedData()).get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException("Error decrypting items.", e);
                        }
                        JsonObject data = Constants.GSON.fromJson(decryptedJson, JsonObject.class);

                        // Get the parent folder id
                        String parentFolderIdString = data.get("folder").getAsString();
                        UUID parentFolderId;
                        if (!parentFolderIdString.equals(Folder.NO_PARENT_FOLDER_ID)) {
                            parentFolderId = UUID.fromString(parentFolderIdString);
                        } else {
                            parentFolderId = null;
                        }

                        Folder folder = new Folder(
                                itemData.getId(),
                                data.get("title").getAsString(),
                                itemData.getCreated(),
                                itemData.getLastModified()
                        );

                        parentFolderMap.put(folder, parentFolderId);
                        return folder;
                    }));
            return new Pair<>(parentFolderMap, folderMap);
        }, EXECUTOR_SERVICE);
    }

    private static CompletableFuture<List<Folder>>
    getRootFolders(Map<UUID, Folder> folderMap, Map<Folder, UUID> parentFolderMap) {
        return CompletableFuture.supplyAsync(() -> {
            List<Folder> rootFolders = new ArrayList<>();

            // Build the list of root folders and set the subfolder references.
            parentFolderMap.forEach((folder, parentFolderId) -> {
                if (parentFolderId == null) {
                    rootFolders.add(folder);
                } else {
                    Folder parentFolder = folderMap.get(parentFolderId);
                    folder.setFolder(parentFolder);
                    parentFolder.addSubFolder(folder);
                }
            });

            return rootFolders;
        }, EXECUTOR_SERVICE);
    }

    private static CompletableFuture<Map<ItemType, List<LeafItem>>>
    getLeafItemMap(Collection<ItemData> itemDataCollection, Map<UUID, Folder> folderMap) {
        return CompletableFuture.supplyAsync(() ->
            itemDataCollection.parallelStream()
            .filter(item -> item.getType() != ItemType.FOLDER)
            .map(itemData -> {
                String decryptedJson;
                try {
                    decryptedJson = ClientSecurity.decrypt(itemData.getEncryptedData()).get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Error decrypting item data.", e);
                }

                JsonObject data = Constants.GSON.fromJson(decryptedJson, JsonObject.class);
                UUID folderId = UUID.fromString(data.get("folder").getAsString());

                Folder folder = folderMap.get(folderId);
                LeafItem item = ITEM_BUILDERS.get(itemData.getType()).apply(itemData, folder, data);
                folder.addItem(item);

                return item;
            })
            .collect(groupingBy(LeafItem::getType, () -> new EnumMap<>(ItemType.class), toList())
        ), EXECUTOR_SERVICE);
    }

    private ItemParser() {
        throw new AssertionError();
    }
}
