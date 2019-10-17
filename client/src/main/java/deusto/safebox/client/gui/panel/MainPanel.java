package deusto.safebox.client.gui.panel;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;

public class MainPanel extends JPanel {

    public MainPanel() {
        super(new BorderLayout());

        JTree folderTree = new JTree();
        JScrollPane folderTreeScrollPane = new JScrollPane(folderTree);

        JTree itemTree = new JTree();
        JScrollPane itemTreeScrollPane = new JScrollPane(itemTree);

        JTable table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setRequestFocusEnabled(false);
        tabbedPane.addTab("Folders", folderTreeScrollPane);
        tabbedPane.addTab("Categories", itemTreeScrollPane);

        JTextArea exampleTestArea = new JTextArea();

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, exampleTestArea);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, rightSplitPane);

        add(mainSplitPane, BorderLayout.CENTER);
    }
}
