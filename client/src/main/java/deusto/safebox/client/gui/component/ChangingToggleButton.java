package deusto.safebox.client.gui.component;

import static deusto.safebox.client.util.IconManager.IconType;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 * A {@link JToggleButton} with a different icon for its on and off state.
 */
public class ChangingToggleButton extends JToggleButton {

    private final ImageIcon onIcon;
    private final ImageIcon offIcon;

    /**
     * Creates a toggle button with the specified icons and selection state.
     *
     * @param onIconType the icon that the button displays on its selected state.
     * @param offIconType the icon that the button displays on its unselected state.
     * @param selected if true, the button is initially selected;
     *                 otherwise, the button is initially unselected.
     * @param onAction action callback when the button is toggled on.
     * @param offAction action callback when the button is toggled ff.
     */
    public ChangingToggleButton(IconType onIconType, IconType offIconType, boolean selected, Runnable onAction, Runnable offAction) {
        setFocusPainted(false);
        setRequestFocusEnabled(false);

        onIcon = onIconType.getAsIcon();
        offIcon = offIconType.getAsIcon();

        addActionListener(e -> {
            if (isSelected()) {
                setIcon(onIcon);
                onAction.run();
            } else {
                setIcon(offIcon);
                offAction.run();
            }
        });

        setSelected(selected);

        if (selected) {
            setIcon(onIcon);
        } else {
            setIcon(offIcon);
        }
    }
}
