package deusto.safebox.client.gui.menu;

import deusto.safebox.client.gui.component.SearchBox;
import deusto.safebox.client.gui.panel.PassGenDialog;
import deusto.safebox.client.gui.panel.SettingsDialog;
import deusto.safebox.client.util.IconType;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar {

    public ToolBar(JFrame owner, Runnable lockAction) {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));

        add(new ToolBarButton("Lock database", IconType.LOCK, lockAction));
        add(new ToolBarButton("Password generator", IconType.PASSWORD_FIELD, () -> new PassGenDialog(owner)));
        addSeparator();
        add(new ToolBarButton("Settings", IconType.GEAR, () -> new SettingsDialog(owner)));
        addSeparator();
        add(Box.createHorizontalGlue());
        add(new SearchBox());
    }

    private static class ToolBarButton extends JButton {

        ToolBarButton(String toolTipText, IconType iconType, Runnable action) {
            super(iconType.getAsIcon());
            setFocusPainted(false);
            setFocusable(false);
            setToolTipText(toolTipText);
            addActionListener(e -> action.run());
        }
    }
}
