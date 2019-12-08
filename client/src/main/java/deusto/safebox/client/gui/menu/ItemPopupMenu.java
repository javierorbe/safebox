package deusto.safebox.client.gui.menu;

import deusto.safebox.client.util.IconType;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ItemPopupMenu extends JPopupMenu {

    public ItemPopupMenu(Runnable newItemAction, Runnable newFolderAction, Runnable deleteFolderAction) {
        // TODO: set custom icons for each menu item
        add(new PopupMenuItem("New item", IconType.NEW_FILE_12, newItemAction));
        addSeparator();
        add(new PopupMenuItem("New folder", IconType.NEW_FILE_12, newFolderAction));
        add(new PopupMenuItem("Delete folder", IconType.NEW_FILE_12, deleteFolderAction));
    }

    private static class PopupMenuItem extends JMenuItem {

        private PopupMenuItem(String text, IconType icon, Runnable action) {
            super(text, icon.getAsIcon());
            addActionListener(e -> action.run());
        }
    }
}
