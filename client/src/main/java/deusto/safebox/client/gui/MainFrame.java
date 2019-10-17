package deusto.safebox.client.gui;

import deusto.safebox.client.gui.menu.MenuBar;
import deusto.safebox.client.gui.panel.MainPanel;
import deusto.safebox.client.util.GuiUtil;
import deusto.safebox.client.util.IconManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

    private static final Dimension PREFERRED_SIZE = new Dimension(1280, 720);

    private JPanel currentContentPanel = null;

    public MainFrame() {
        super("SafeBox");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLookAndFeel();
        setPreferredSize(PREFERRED_SIZE);
        setIconImage(IconManager.getAsImage(IconManager.IconType.APP));
        setLayout(new BorderLayout());
        setJMenuBar(new MenuBar());

        JPanel mainPanel = new MainPanel();
        setCurrentPanel(mainPanel);

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }

    public void setCurrentPanel(JPanel panel) {
        if (currentContentPanel != null) {
            getContentPane().remove(currentContentPanel);
        }
        getContentPane().add(panel, BorderLayout.CENTER);
        currentContentPanel = panel;
    }

    /** Set the default look and feel. */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.error("Could not load the default look and feel.", e);
        }
    }
}
