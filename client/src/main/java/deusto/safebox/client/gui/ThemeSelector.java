package deusto.safebox.client.gui;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThemeSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeSelector.class);

    public static void setTheme(Theme theme) {
        if (theme == null) {
            setDefaultTheme();
        } else {
            try {
                UIManager.setLookAndFeel(theme.classname);
                if (theme.metalTheme != null) {
                    MetalLookAndFeel.setCurrentTheme(theme.metalTheme);
                }
            } catch (ClassNotFoundException | InstantiationException
                    | IllegalAccessException | UnsupportedLookAndFeelException e) {
                LOGGER.error("Error setting theme: " + theme.id, e);
                setDefaultTheme();
            }
        }
    }

    public static void setDefaultTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            LOGGER.error("Error setting default theme.", e);
        }
    }

    public enum Theme {
        WINDOWS("windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"),
        MOTIF("motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel"),
        METAL("metal", "javax.swing.plaf.metal.MetalLookAndFeel", new DefaultMetalTheme()),
        OCEAN("ocean", "javax.swing.plaf.metal.MetalLookAndFeel", new OceanTheme()),
        GTK("gtk", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"),
        ;

        private static final Map<String, Theme> ID_MAPPER
                = Arrays.stream(values()).collect(toMap(Theme::getId, e -> e));

        private final String id;
        private final String classname;
        private final MetalTheme metalTheme;

        Theme(String id, String classname, MetalTheme metalTheme) {
            this.id = id;
            this.classname = classname;
            this.metalTheme = metalTheme;
        }

        Theme(String id, String classname) {
            this(id, classname, null);
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return id;
        }

        public static Theme fromId(String id) {
            return ID_MAPPER.get(id);
        }
    }
}
