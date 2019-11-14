package deusto.safebox.client.gui.panel;

import deusto.safebox.client.datamodel.Folder;
import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.datamodel.Login;
import deusto.safebox.client.datamodel.Note;
import deusto.safebox.client.gui.component.DataTable;
import deusto.safebox.client.gui.component.FolderTree;
import deusto.safebox.client.gui.component.ItemTree;
import deusto.safebox.common.ItemType;
import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;

public class MainPanel extends JPanel {

    // TODO: remove this example collections and use the ItemManager.
    private static final List<Folder> exampleFolders = new ArrayList<>();
    private static final Map<ItemType, List<LeafItem>> exampleItems = new EnumMap<>(ItemType.class);

    // TEMP
    static {
        for (ItemType value : ItemType.values()) {
            exampleItems.put(value, new ArrayList<>());
        }

        Folder f1 = new Folder("Folder1");
        Folder f2 = new Folder("Folder2");
        Folder f3 = new Folder("Folder3");
        Folder f4 = new Folder("Folder4");
        Folder f5 = new Folder("Folder5");

        Login login = new Login(
                "ExampleLogin",
                f1,
                LocalDateTime.of(2019, 2, 7, 14, 15),
                LocalDateTime.of(2019, 2, 7, 14, 15),
                "MyUsername",
                "MySecretPassword",
                "http://www.website.com",
                LocalDate.of(2020, 5, 7)
        );

        Note note = new Note(
                "ExampleNote",
                f2,
                LocalDateTime.of(2019, 2, 7, 14, 15),
                LocalDateTime.of(2019, 2, 7, 14, 15),
                "My secret note content."
        );

        f1.addSubFolder(f2);
        f2.addSubFolder(f3);
        f4.addSubFolder(f5);

        exampleFolders.add(f1);
        exampleFolders.add(f4);
        exampleItems.get(ItemType.LOGIN).add(login);
        exampleItems.get(ItemType.NOTE).add(note);
    }

    public MainPanel() {
        super(new BorderLayout());

        DataTable table = new DataTable(exampleItems);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JTree folderTree = new FolderTree(exampleFolders, table);
        JScrollPane folderTreeScrollPane = new JScrollPane(folderTree);

        JTree itemTree = new ItemTree(exampleItems, table);
        JScrollPane itemTreeScrollPane = new JScrollPane(itemTree);

        JTabbedPane treeTabbedPane = new JTabbedPane();
        treeTabbedPane.setRequestFocusEnabled(false);
        treeTabbedPane.addTab("Folders", folderTreeScrollPane);
        treeTabbedPane.addTab("Categories", itemTreeScrollPane);

        JTextArea exampleTestArea = new JTextArea();

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, exampleTestArea);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTabbedPane, rightSplitPane);

        add(mainSplitPane, BorderLayout.CENTER);
        expandAll(folderTree);
    }

    private static void expandAll(JTree tree){
        int i = 0;
        if(tree != null) {
            expandAll(tree, i);
        }
    }

    private static void expandAll(JTree tree, int i){
        if (i < tree.getRowCount()) {
            if (tree.isCollapsed(i)) {
                tree.expandRow(i);
            }
            expandAll(tree, ++i);
        }
    }
}