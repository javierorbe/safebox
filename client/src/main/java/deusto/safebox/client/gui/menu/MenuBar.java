package deusto.safebox.client.gui.menu;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {

    public MenuBar(Runnable saveAction, Runnable changeMasterPwdAction, Runnable logOutAction) {
        JMenu accountMenu = new JMenu("Account");
        add(accountMenu);

        accountMenu.add(new MenuBarItem("Save data", saveAction));
        accountMenu.add(new MenuBarItem("Change master password", changeMasterPwdAction));
        accountMenu.add(new MenuBarItem("Log out", logOutAction));

        JMenu helpMenu = new JMenu("Help");
        add(helpMenu);

        helpMenu.add(new MenuBarItem("Help", () -> {
            // TODO
        }));

        helpMenu.add(new MenuBarItem("About", () -> {
            // TODO
        }));
    }

    private static class MenuBarItem extends JMenuItem {

        /**
         * Creates a {@link MenuBarItem} with an specified action callback.
         *
         * @param text the text displayed in the item.
         * @param icon the icon displayed in the item.
         * @param action item action callback.
         */
        MenuBarItem(String text, Icon icon, Runnable action) {
            super(text, icon);
            addActionListener(e -> action.run());
        }

        MenuBarItem(String text, Runnable action) {
            this(text, null, action);
        }
    }
}
