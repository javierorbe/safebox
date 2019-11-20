package deusto.safebox.client.gui;

import deusto.safebox.client.ItemManager;
import deusto.safebox.client.ItemParser;
import deusto.safebox.client.gui.menu.MenuBar;
import deusto.safebox.client.gui.menu.ToolBar;
import deusto.safebox.client.gui.panel.AuthPanel;
import deusto.safebox.client.gui.panel.MainPanel;
import deusto.safebox.client.net.Client;
import deusto.safebox.client.net.PacketHandler;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.ItemData;
import deusto.safebox.common.net.packet.LogOutPacket;
import deusto.safebox.common.net.packet.ReceiveDataPacket;
import deusto.safebox.common.net.packet.SaveDataPacket;
import deusto.safebox.common.util.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private final Map<PanelType, JPanel> panels = new EnumMap<>(PanelType.class);
    private PanelType currentPanel;

    public MainFrame(Client client) {
        super("SafeBox");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GuiUtil.setDefaultLookAndFeel();
        setPreferredSize(new Dimension(1280, 720));
        setIconImage(IconType.APP.getAsImage());
        setLayout(new BorderLayout());

        Runnable logOut = () -> {
            if (currentPanel == PanelType.MAIN) {
                setCurrentPanel(PanelType.AUTH);
                client.sendPacket(new LogOutPacket());
            }
        };

        setJMenuBar(new MenuBar(
                () -> new Thread(() -> {
                    Collection<ItemData> items = ItemParser.toItemData(ItemManager.INSTANCE.getAll());
                    client.sendPacket(new SaveDataPacket(items));
                }).start(),
                () -> { /* TODO */ },
                logOut
        ));

        getContentPane().add(new ToolBar(this, logOut), BorderLayout.PAGE_START);

        // Load all panels
        panels.put(PanelType.MAIN, new MainPanel(this));
        panels.put(PanelType.AUTH, new AuthPanel(client::sendPacket, client::sendPacket));

        // Set initial panel
        currentPanel = PanelType.AUTH;
        getContentPane().add(panels.get(PanelType.MAIN), BorderLayout.CENTER);

        PacketHandler.INSTANCE.addListener(ReceiveDataPacket.class, ignored -> setCurrentPanel(PanelType.MAIN));

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
