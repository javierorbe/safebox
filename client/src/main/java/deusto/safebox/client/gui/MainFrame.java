package deusto.safebox.client.gui;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.ItemParser;
import deusto.safebox.client.gui.menu.MenuBar;
import deusto.safebox.client.gui.menu.ToolBar;
import deusto.safebox.client.gui.panel.AuthPanel;
import deusto.safebox.client.gui.panel.MainPanel;
import deusto.safebox.client.net.Client;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.net.packet.SaveDataPacket;
import deusto.safebox.common.util.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private static final Dimension PREFERRED_SIZE = new Dimension(1280, 720);

    private final Map<PanelType, JPanel> panels = new EnumMap<>(PanelType.class);
    private PanelType currentPanel;

    public MainFrame(Client client) {
        super("SafeBox");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GuiUtil.setDefaultLookAndFeel();
        setPreferredSize(PREFERRED_SIZE);
        setIconImage(IconType.APP.getAsImage());
        setLayout(new BorderLayout());

        JMenuBar menuBar = new MenuBar(
                new Thread(() -> {
                    Collection<ItemData> items = ItemParser.toItemData(ItemManager.INSTANCE.getAll());
                    client.sendPacket(new SaveDataPacket(items));
                })::start,
                () -> { /* TODO */ },
                () -> { /* TODO */ }
        );
        setJMenuBar(menuBar);

        getContentPane().add(new ToolBar(this, () -> {
            // TEMP
            if (currentPanel == PanelType.MAIN) {
                setCurrentPanel(PanelType.AUTH);
            } else {
                setCurrentPanel(PanelType.MAIN);
            }
        }), BorderLayout.PAGE_START);

        // Load all panels
        panels.put(PanelType.MAIN, new MainPanel(this));
        AuthPanel authPanel = new AuthPanel(client::sendPacket, client::sendPacket);
        panels.put(PanelType.AUTH, authPanel);

        // Set initial panel
        currentPanel = PanelType.AUTH;
        getContentPane().add(panels.get(PanelType.AUTH), BorderLayout.CENTER);

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }

    private void setCurrentPanel(PanelType panel) {
        getContentPane().remove(panels.get(currentPanel));
        getContentPane().add(panels.get(panel), BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();
        currentPanel = panel;
    }

    /** Content panel types. */
    private enum PanelType {
        MAIN, AUTH
    }
}
