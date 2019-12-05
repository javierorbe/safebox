package deusto.safebox.common.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiUtil {

    private static final Logger logger = LoggerFactory.getLogger(GuiUtil.class);

    /** Returns the center location for the component, relative to the screen size. */
    public static Point getCenteredLocation(Component component) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(
                dimension.width / 2 - component.getWidth() / 2,
                dimension.height / 2 - component.getHeight() / 2
        );
    }

    /** Returns the center location for the component, relative to another component. */
    public static Point getCenteredLocation(Component owner, Component component) {
        Point ownerLoc = owner.getLocation();
        return new Point(
                (int) ownerLoc.getX() + owner.getWidth() / 2 - component.getWidth() / 2,
                (int) ownerLoc.getY() + owner.getHeight() / 2 - component.getHeight() / 2
        );
    }

    /** Sets the look and feel to the system default. */
    public static void setDefaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.error("Could not load the default look and feel.", e);
        }
    }

    public static void runSwing(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    private GuiUtil() {
        throw new AssertionError();
    }
}
