package deusto.safebox.client.gui;

import static deusto.safebox.common.util.GuiUtil.runSwing;

import deusto.safebox.client.ClientMain;
import deusto.safebox.client.ItemManager;
import deusto.safebox.client.datamodel.ItemParser;
import deusto.safebox.client.gui.menu.MenuBar;
import deusto.safebox.client.gui.menu.ToolBar;
import deusto.safebox.client.gui.panel.AuthPanel;
import deusto.safebox.client.gui.panel.IndeterminateProgressDialog;
import deusto.safebox.client.gui.panel.MainPanel;
import deusto.safebox.client.net.Client;
import deusto.safebox.client.net.PacketHandler;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.net.packet.DisconnectPacket;
import deusto.safebox.common.net.packet.LogOutPacket;
import deusto.safebox.common.net.packet.RetrieveDataPacket;
import deusto.safebox.common.net.packet.SaveDataPacket;
import deusto.safebox.common.util.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executors;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MainFrame extends JFrame {

    private final Client client;
    private final Map<PanelType, JPanel> panels = new EnumMap<>(PanelType.class);
    private PanelType currentPanel;

    public MainFrame(Client client) {
        super("SafeBox");
        this.client = client;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLookAndFeel();
        setPreferredSize(new Dimension(1280, 720));
        setIconImage(IconType.SAFEBOX.getAsImage());
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (client.isRunning()) {
                    client.sendPacket(new DisconnectPacket());
                }
            }
        });

        Runnable logOut = () -> {
            if (currentPanel == PanelType.MAIN) {
                runSwing(() -> setCurrentPanel(PanelType.AUTH));
                Executors.newSingleThreadExecutor().submit(() -> client.sendPacket(new LogOutPacket()));

                ((MainPanel) panels.get(PanelType.MAIN)).getFolderTree().removeRootChildren();
                ((MainPanel) panels.get(PanelType.MAIN)).getItemTree().getRoot().removeAllChildren();
            }
        };

        // Create menus
        getContentPane().add(new ToolBar(this, logOut), BorderLayout.PAGE_START);
        setJMenuBar(new MenuBar(this::saveData, logOut));

        // Load all panels
        panels.put(PanelType.MAIN, new MainPanel(this));
        panels.put(PanelType.AUTH, new AuthPanel(client::sendPacket, client::sendPacket));

        // Set initial panel
        currentPanel = PanelType.AUTH;
        getContentPane().add(panels.get(PanelType.AUTH), BorderLayout.CENTER);

        PacketHandler.INSTANCE.registerListener(RetrieveDataPacket.class, e -> setCurrentPanel(PanelType.MAIN));
        TrayIconHandler.init(this, () -> {
            client.sendPacket(new DisconnectPacket());
            dispose();
        });

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }

    private void setLookAndFeel() {
        String themeId = ClientMain.CONFIG.getString("theme");
        ThemeSelector.Theme theme = ThemeSelector.Theme.fromId(themeId);
        ThemeSelector.setTheme(theme);
    }

    public void setCurrentPanel(PanelType panel) {
        getContentPane().remove(panels.get(currentPanel));
        getContentPane().add(panels.get(panel), BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();
        currentPanel = panel;
    }

    private void saveData() {
        if (currentPanel != PanelType.MAIN) {
            return;
        }

        JDialog dialog = new IndeterminateProgressDialog(this, "Saving data...");
        runSwing(() -> dialog.setVisible(true));

        ItemParser.toItemData(ItemManager.getAll())
                .thenAccept(items -> {
                    client.sendPacket(new SaveDataPacket(items));
                    runSwing(dialog::dispose);
                });
    }

    /** Content panel types. */
    public enum PanelType {
        AUTH, // Authentication
        MAIN,
    }
}
