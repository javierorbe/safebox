package deusto.safebox.client.gui.menu;

import deusto.safebox.client.gui.TrayIconHandler;
import deusto.safebox.client.gui.component.SearchBox;
import deusto.safebox.client.gui.panel.SettingsDialog;
import deusto.safebox.client.gui.panel.pwdgen.PassGenDialog;
import deusto.safebox.client.util.IconType;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar {

    public ToolBar(JFrame mainFrame, Runnable lockAction) {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));

        add(new ToolBarButton("New item", IconType.NEW_FILE_20, () -> { /* TODO */ }));
        addSeparator();
        add(new ToolBarButton("Lock database", IconType.LOCK, lockAction));
        add(new ToolBarButton("Minimize", IconType.MINIMIZE, () -> {
            mainFrame.setVisible(false);
            TrayIconHandler.showTrayIcon();
        }));
        addSeparator();
        add(new ToolBarButton("Password generator", IconType.PASSWORD_FIELD, () -> new PassGenDialog(mainFrame)));
        add(new ToolBarButton("Settings", IconType.GEAR, () -> new SettingsDialog(mainFrame)));
        addSeparator();
        add(Box.createHorizontalGlue());
        add(new SearchBox());
    }

    private static class ToolBarButton extends JButton {

        ToolBarButton(String toolTipText, IconType iconType, Runnable action) {
            super(iconType.getAsIcon());
            setFocusPainted(false);
            setRequestFocusEnabled(false);
            setToolTipText(toolTipText);
            addActionListener(e -> action.run());
        }
    }
}
