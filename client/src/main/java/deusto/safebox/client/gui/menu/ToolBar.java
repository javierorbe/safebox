package deusto.safebox.client.gui.menu;

import static deusto.safebox.client.util.IconManager.IconType;

import deusto.safebox.client.gui.component.SearchBox;
import deusto.safebox.client.gui.panel.SettingsDialog;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar {

    public ToolBar(JFrame mainFrame, Runnable lockAction) {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));

        JButton newEntryBtn = new ToolBarButton(
            "Add a new entry",
            IconType.NEW_FILE_20,
            () -> { /* TODO */ }
        );

        final JButton lockBtn = new ToolBarButton("Lock database", IconType.LOCK, lockAction);

        final JToggleButton pwdGenBtn = new ToolBarToggleButton(
            "Password generator",
            IconType.PASSWORD_FIELD,
            () -> { /* TODO */ }
        );

        final JButton settingsBtn = new ToolBarButton(
            "Settings",
            IconType.GEAR,
            () -> new SettingsDialog(mainFrame)
        );

        final SearchBox searchBox = new SearchBox();

        add(newEntryBtn);
        addSeparator();
        add(pwdGenBtn);
        add(lockBtn);
        addSeparator();
        add(settingsBtn);
        addSeparator();
        add(Box.createHorizontalGlue());
        add(searchBox);
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

    private static class ToolBarToggleButton extends JToggleButton {

        ToolBarToggleButton(String toolTipText, IconType iconType, Runnable action) {
            super(iconType.getAsIcon());
            setFocusPainted(false);
            setRequestFocusEnabled(false);
            setToolTipText(toolTipText);
            addActionListener(e -> action.run());
        }
    }
}
