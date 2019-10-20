package deusto.safebox.client.gui.component;

import static deusto.safebox.client.util.IconManager.IconType;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public abstract class ChangingToggleButton extends JToggleButton {

    private final ImageIcon onIcon;
    private final ImageIcon offIcon;

    public ChangingToggleButton(IconType onIconType, IconType offIconType, boolean selected) {
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

    public abstract void action(boolean state);
}
