package deusto.safebox.client.util;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum IconType {

    // Maintain lexicographical order on the type names
    APP("app"),
    EYE("eye"),
    EYE_CLOSED("eye_closed"),
    FOLDER("folder_16"),
    GEAR("gear_20"),
    LOCK("lock_20"),
    NEW_FILE_12("new_file_12"),
    NEW_FILE_16("new_file_16"),
    NEW_FILE_20("new_file_20"),
    PASSWORD_FIELD("password_field_20"),

    INFO_BOOK("info_book"),
    STRONGBOX("strongbox"),
    ;

    private final Image image;
    private final ImageIcon imageIcon;

    IconType(String filename) {
        this.image = load(filename);
        imageIcon = new ImageIcon(Objects.requireNonNull(image));
    }

    public Image getAsImage() {
        return image;
    }

    public ImageIcon getAsIcon() {
        return imageIcon;
    }

    private static Image load(String filename) {
        URL url = IconType.class.getResource("/img/" + filename + ".png");
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the image " + filename, e);
        }
    }
}
