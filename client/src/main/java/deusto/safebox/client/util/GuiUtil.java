package deusto.safebox.client.util;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public class GuiUtil {

    private GuiUtil() {
        throw new AssertionError();
    }

    public static Point getCenteredLocation(Component component) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(
                dimension.width / 2 - component.getWidth() / 2,
                dimension.height / 2 - component.getHeight() / 2
        );
    }
}
