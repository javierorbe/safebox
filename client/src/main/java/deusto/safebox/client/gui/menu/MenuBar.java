package deusto.safebox.client.gui.menu;

import deusto.safebox.client.util.IconType;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {

    public MenuBar(Runnable saveAction, Runnable logOutAction) {
        JMenu accountMenu = new JMenu("Account");
        add(accountMenu);

        accountMenu.add(new MenuBarItem("Save data", saveAction));
        accountMenu.add(new MenuBarItem("Log out", logOutAction));

        JMenu helpMenu = new JMenu("Help");
        add(helpMenu);

        helpMenu.add(new MenuBarItem("Help", IconType.INFO_BOOK, () -> { /* TODO */ }));
        helpMenu.add(new MenuBarItem("About", () -> { /* TODO */ }));
    }

    private static class MenuBarItem extends JMenuItem {

        MenuBarItem(String text, IconType icon, Runnable action) {
            super(text, icon.getAsIcon());
            addActionListener(e -> action.run());
        }

        MenuBarItem(String text, Runnable action) {
            super(text);
            addActionListener(e -> action.run());
        }
    }
}
