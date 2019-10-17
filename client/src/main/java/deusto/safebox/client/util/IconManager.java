package deusto.safebox.client.util;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IconManager {

    private static final Logger logger = LoggerFactory.getLogger(IconManager.class);

    static {
        for (IconType i : IconType.values()) {
            URL url = IconManager.class.getResource("/img/" + i.filename + ".png");
            try {
                i.image = ImageIO.read(url);
            } catch (IOException e) {
                logger.error("Could load image " + i.filename + ".", e);
            }
        }
    }

    public static Image getAsImage(IconType iconType) {
        return iconType.image;
    }

    public static ImageIcon getAsIcon(IconType iconType) {
        return new ImageIcon(getAsImage(iconType));
    }

    public enum IconType {
        APP("app"),
        ;

        private String filename;
        private Image image;

        IconType(String filename) {
            this.filename = filename;
        }
    }

    private IconManager() {
        throw new AssertionError();
    }
}
