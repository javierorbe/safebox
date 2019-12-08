package deusto.safebox.client.gui.menu;

import deusto.safebox.client.util.IconType;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ItemPopupMenu extends JPopupMenu {

    public ItemPopupMenu(Runnable newItemAction, Runnable editItemAction, Runnable deleteItemAction,
                         Runnable newFolderAction, Runnable editFolderAction, Runnable deleteFolderAction) {
        // TODO: set custom icons for each menu item
        add(new PopupMenuItem("New item", IconType.NEW_FILE_12, newItemAction));
        add(new PopupMenuItem("Edit item", IconType.NEW_FILE_12, editItemAction));
        add(new PopupMenuItem("Delete item", IconType.NEW_FILE_12, deleteItemAction));
        addSeparator();
        add(new PopupMenuItem("New folder", IconType.NEW_FILE_12, newFolderAction));
        add(new PopupMenuItem("Edit folder", IconType.NEW_FILE_12, editFolderAction));
        add(new PopupMenuItem("Delete folder", IconType.NEW_FILE_12, deleteFolderAction));
    }

    private static class PopupMenuItem extends JMenuItem {

        private PopupMenuItem(String text, IconType icon, Runnable action) {
            super(text, icon.getAsIcon());
            addActionListener(e -> action.run());
        }
    }
}
