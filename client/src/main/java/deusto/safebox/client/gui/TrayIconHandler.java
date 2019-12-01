package deusto.safebox.client.gui;

import deusto.safebox.client.util.IconType;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrayIconHandler {

    private static final Logger logger = LoggerFactory.getLogger(TrayIconHandler.class);

    private static JFrame mainFrame;
    private static Runnable closeCallback;

    private static TrayIcon trayIcon = null;

    public static void init(JFrame theMainFrame, Runnable theCloseCallback) {
        mainFrame = theMainFrame;
        closeCallback = theCloseCallback;
    }

    public static void showTrayIcon() {
        if (!SystemTray.isSupported()) {
            logger.info("System tray is not supported.");
            return;
        }

        PopupMenu menu = new TrayPopupMenu();
        Image icon = IconType.SAFEBOX.getAsImage();

        trayIcon = new TrayIcon(icon, "SafeBox", menu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    hideTrayIcon();
                    mainFrame.setVisible(true);
                }
            }
        });

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            logger.error("Error adding the tray icon.", e);
        }
    }

    public static void hideTrayIcon() {
        if (SystemTray.isSupported() && trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
        }
    }

    @SuppressWarnings("serial")
    private static class TrayPopupMenu extends PopupMenu {

        private TrayPopupMenu() {
            add(new PopupMenuItem("Close", () -> {
                hideTrayIcon();
                closeCallback.run();
            }));
        }
    }

    @SuppressWarnings("serial")
    private static class PopupMenuItem extends MenuItem {

        private PopupMenuItem(String text, Runnable action) {
            super(text);
            addActionListener(e -> action.run());
        }
    }

    private TrayIconHandler() {
        throw new AssertionError();
    }
}
