package deusto.safebox.client;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.google.gson.JsonObject;
import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.datamodel.Login;
import deusto.safebox.client.datamodel.Note;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ItemParser {

    private static final Map<ItemType, TriFunction<ItemData, Folder, JsonObject, LeafItem>> ITEM_BUILDERS
            = new EnumMap<>(ItemType.class);

    static {
        ITEM_BUILDERS.put(ItemType.LOGIN, Login::of);
        ITEM_BUILDERS.put(ItemType.NOTE, Note::of);
    }

    /**
     * Transforms a collection of {@link AbstractItem} into a collection of {@link ItemData}.
     *
     * @param items the {@link AbstractItem} collection.
     * @return the {@link ItemData} collection.
     */
    public static Collection<ItemData> toItemData(Collection<AbstractItem> items) {
        return items
                .parallelStream()
                .map(ItemData::new)
                .collect(toSet());
    }

    /**
     * Transforms a collection of {@link ItemData}
     * into a list of root folders and a map of {@link LeafItem}s grouped by type.
     *
     * @param itemDataCollection the {@link ItemData} collection.
     * @return a {@link Pair} containing the root folder list and the item map.
     */
    public static Pair<List<Folder>, Map<ItemType, List<LeafItem>>>
    fromItemData(Collection<ItemData> itemDataCollection) {
        // <folder, parent folder id>
        Map<Folder, UUID> parentFolderMap = new HashMap<>();

        // Collect all the folders into a map where the key is the folder id and the value is the folder instance.
        // <folder id, folder>
        Map<UUID, Folder> folderMap = itemDataCollection.parallelStream()
                .filter(item -> item.getType() == ItemType.FOLDER)
                .collect(toMap(ItemData::getId, itemData -> {
                    String decryptedJson = ClientSecurity.decrypt(itemData.getEncryptedData());
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

        // Build the map of leaf items grouped by type.
        Map<ItemType, List<LeafItem>> leafItems = itemDataCollection.parallelStream()
                .filter(item -> item.getType() != ItemType.FOLDER)
                .map(itemData -> {
                    String decryptedJson = ClientSecurity.decrypt(itemData.getEncryptedData());
                    JsonObject data = Constants.GSON.fromJson(decryptedJson, JsonObject.class);
                    UUID folderId = UUID.fromString(data.get("folder").getAsString());

                    Folder folder = folderMap.get(folderId);
                    LeafItem item = ITEM_BUILDERS.get(itemData.getType()).apply(itemData, folder, data);
                    folder.addItem(item);

                    return item;
                })
                .collect(groupingBy(
                        LeafItem::getType,
                        () -> new EnumMap<>(ItemType.class),
                        toList()
                ));

        return new Pair<>(rootFolders, leafItems);
    }

    private ItemParser() {
        throw new AssertionError();
    }
}
