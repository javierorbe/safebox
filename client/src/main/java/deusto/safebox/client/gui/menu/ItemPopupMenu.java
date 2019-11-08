package deusto.safebox.client.gui.menu;

import deusto.safebox.client.util.IconType;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ItemPopupMenu extends JPopupMenu {

    protected ItemPopupMenu() {
        final JMenuItem newItem = new JMenuItem("New item", IconType.NEW_FILE_12.getAsIcon());
        final JMenuItem editItem = new JMenuItem("Edit item", IconType.NEW_FILE_12.getAsIcon());
        final JMenuItem deleteItem = new JMenuItem("Delete item", IconType.NEW_FILE_12.getAsIcon());

        final JMenuItem copyUsername = new JMenuItem("Copy username", IconType.NEW_FILE_12.getAsIcon());
        final JMenuItem copyPassword = new JMenuItem("Copy password", IconType.NEW_FILE_12.getAsIcon());

        newItem.addActionListener(e -> { /* TODO */ });
        editItem.addActionListener(e -> { /* TODO */ });
        deleteItem.addActionListener(e -> { /* TODO */ });

        add(newItem);
        add(editItem);
        add(deleteItem);
        addSeparator();
        add(copyUsername);
        add(copyPassword);
    }
}
