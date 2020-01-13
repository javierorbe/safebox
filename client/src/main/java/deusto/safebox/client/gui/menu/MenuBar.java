package deusto.safebox.client.gui.menu;

import deusto.safebox.client.locale.Message;
import deusto.safebox.client.util.IconType;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {

    public MenuBar(Runnable saveAction, Runnable logOutAction) {
        JMenu accountMenu = new JMenu(Message.ACCOUNT.get());
        add(accountMenu);

        accountMenu.add(new MenuBarItem(Message.SAVE_DATA.get(), saveAction));
        accountMenu.add(new MenuBarItem(Message.SIGN_OUT.get(), logOutAction));

        JMenu helpMenu = new JMenu(Message.HELP.get());
        add(helpMenu);

        helpMenu.add(new MenuBarItem(Message.HELP.get(), IconType.INFO_BOOK, () -> { /* TODO */ }));
        helpMenu.add(new MenuBarItem(Message.ABOUT.get(), () -> { /* TODO */ }));
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
