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

    public enum IconType {
        // Maintain lexicographical order on the type names
        APP("app"),
        EYE("eye"),
        EYE_CLOSED("eye_closed"),
        FOLDER("folder_16"),
        GEAR("gear_20"),
        LOCK("lock_20"),
        NEW_FILE_16("new_file_16"),
        NEW_FILE_20("new_file_20"),
        PASSWORD_FIELD("password_field_20"),
        ;

        private final String filename;
        private Image image;

        IconType(String filename) {
            this.filename = filename;
        }

        public Image getAsImage() {
            if (image == null) {
                load();
            }
            return image;
        }

        public ImageIcon getAsIcon() {
            return new ImageIcon(getAsImage());
        }

        private void load() {
            URL url = IconManager.class.getResource("/img/" + filename + ".png");
            try {
                image = ImageIO.read(url);
            } catch (IOException e) {
                logger.error("Could load image " + filename + ".", e);
            }
        }
    }

    private IconManager() {
        throw new AssertionError();
    }
}
