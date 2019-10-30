package deusto.safebox.client.gui;

import static deusto.safebox.client.util.IconManager.IconType;

import deusto.safebox.client.gui.menu.MenuBar;
import deusto.safebox.client.gui.menu.ToolBar;
import deusto.safebox.client.gui.panel.AuthPanel;
import deusto.safebox.client.gui.panel.MainPanel;
import deusto.safebox.common.util.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private static final Dimension PREFERRED_SIZE = new Dimension(1280, 720);

    private Map<PanelType, JPanel> panels = new HashMap<>();
    private PanelType currentPanel;

    public MainFrame() {
        super("SafeBox");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GuiUtil.setDefaultLookAndFeel();
        setPreferredSize(PREFERRED_SIZE);
        setIconImage(IconType.APP.getAsImage());
        setLayout(new BorderLayout());
        setJMenuBar(new MenuBar());

        getContentPane().add(new ToolBar(this) {
            @Override
            protected void lock() {
                // TEMP
                if (currentPanel == PanelType.MAIN) {
                    setCurrentPanel(PanelType.AUTH);
                } else {
                    setCurrentPanel(PanelType.MAIN);
                }
            }
        }, BorderLayout.PAGE_START);

        panels.put(PanelType.MAIN, new MainPanel());
        panels.put(PanelType.AUTH, new AuthPanel());

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
