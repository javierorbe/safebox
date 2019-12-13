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

    private static final Logger LOGGER = LoggerFactory.getLogger(TrayIconHandler.class);

    private static JFrame mainFrame;
    private static Runnable closeCallback;

    private static TrayIcon trayIcon = null;

    /**
     * Initializes the handler.
     *
     * @param theMainFrame the main {@link JFrame}.
     *                     This is the {@code JFrame} that is minimized when the tray icon is shown.
     * @param theCloseCallback callback when the user requests to close the app.
     */
    public static void init(JFrame theMainFrame, Runnable theCloseCallback) {
        mainFrame = theMainFrame;
        closeCallback = theCloseCallback;
    }

    /** Shows the tray icon and hides the main frame. */
    public static void showTrayIcon() {
        if (!SystemTray.isSupported()) {
            LOGGER.info("System tray is not supported.");
            return;
        }
        buildTrayIcon();
        mainFrame.setVisible(false);
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            LOGGER.error("Error adding the tray icon.", e);
        }
    }

    /** Hides the tray icon and shows the main frame. */
    public static void hideTrayIcon() {
        if (trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
            mainFrame.setVisible(true);
        }
    }

    private static void buildTrayIcon() {
        trayIcon = new TrayIcon(IconType.SAFEBOX.getAsImage(), "SafeBox", new TrayPopupMenu());
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    hideTrayIcon();
                }
            }
        });
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
