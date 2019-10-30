package deusto.safebox.common.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiUtil {

    private static final Logger logger = LoggerFactory.getLogger(GuiUtil.class);

    public static Point getCenteredLocation(Component component) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(
                dimension.width / 2 - component.getWidth() / 2,
                dimension.height / 2 - component.getHeight() / 2
        );
    }

    /** Set the system default look and feel. */
    public static void setDefaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.error("Could not load the default look and feel.", e);
        }
    }

    private GuiUtil() {
        throw new AssertionError();
    }
}
