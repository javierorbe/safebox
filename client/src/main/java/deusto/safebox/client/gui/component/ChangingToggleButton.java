package deusto.safebox.client.gui.component;

import static deusto.safebox.client.util.IconManager.IconType;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 * A {@link JToggleButton} with a different icon for its on and off state.
 */
public abstract class ChangingToggleButton extends JToggleButton {

    private final ImageIcon onIcon;
    private final ImageIcon offIcon;

    /**
     * Creates a toggle button with the specified icons and selection state.
     *
     * @param onIconType the icon that the button displays on its selected state.
     * @param offIconType the icon that the button displays on its unselected state.
     * @param selected if true, the button is initially selected;
     *                 otherwise, the button is initially unselected.
     */
    protected ChangingToggleButton(IconType onIconType, IconType offIconType, boolean selected) {
        setFocusPainted(false);
        setRequestFocusEnabled(false);

        onIcon = onIconType.getAsIcon();
        offIcon = offIconType.getAsIcon();

        addItemListener(e -> {
            if (isSelected()) {
                setIcon(onIcon);
                action(true);
            } else {
                setIcon(offIcon);
                action(false);
            }
        });

        setSelected(selected);

        if (selected) {
            setIcon(onIcon);
        } else {
            setIcon(offIcon);
        }
    }

    /**
     * Button action callback.
     *
     * @param state true if the button is selected, otherwise false.
     */
    public abstract void action(boolean state);
}
