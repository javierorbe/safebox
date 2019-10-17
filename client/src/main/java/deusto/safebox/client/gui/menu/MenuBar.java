package deusto.safebox.client.gui.menu;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {

    public MenuBar() {
        JMenu helpMenu = new JMenu("Help");
        add(helpMenu);

        helpMenu.add(new MenuBarItem("Help") {
            @Override
            void action() {
                // TODO
            }
        });

        helpMenu.add(new MenuBarItem("About") {
            @Override
            void action() {
                // TODO
            }
        });
    }

    private abstract static class MenuBarItem extends JMenuItem {

        MenuBarItem(String text, Icon icon) {
            super(text, icon);
            addActionListener(e -> action());
        }

        MenuBarItem(String text) {
            this(text, null);
        }

        abstract void action();
    }
}
