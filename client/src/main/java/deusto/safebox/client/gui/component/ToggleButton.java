package deusto.safebox.client.gui.component;

import deusto.safebox.client.util.IconType;
import javax.swing.JToggleButton;

public class ToggleButton extends JToggleButton {

    /**
     * Constructs a toggle button with the specified icons and selection state.
     *
     * @param selectedIcon the icon that the button displays when it is selected
     * @param deselectedIcon the icon that the button displays when it is deselected
     * @param selected if true, the button is initially selected;
     *                 otherwise, the button is initially deselected
     * @param selectedAction callback when the button is toggled on
     * @param deselectedAction callback when the button is toggled off
     */
    public ToggleButton(IconType selectedIcon, IconType deselectedIcon,
                        boolean selected, Runnable selectedAction, Runnable deselectedAction) {
        setFocusPainted(false);
        setRequestFocusEnabled(false);
        setSelected(selected);

        setSelectedIcon(selectedIcon.getAsIcon());
        setIcon(deselectedIcon.getAsIcon());

        addActionListener(e -> {
            if (isSelected()) {
                selectedAction.run();
            } else {
                deselectedAction.run();
            }
        });
    }
}
