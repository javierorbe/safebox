package deusto.safebox.client.gui.panel;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.datamodel.property.LongStringProperty;
import deusto.safebox.client.datamodel.property.PasswordProperty;
import deusto.safebox.client.gui.component.ToggleButton;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.gui.component.ScrollTextArea;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.RightAlignedLabel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ItemInfoPanel extends JPanel {

    private final GridBagBuilder gbb = new GridBagBuilder();

    ItemInfoPanel(LeafItem item) {
        super(new GridBagLayout());

        gbb.setFillAndAnchor(GridBagBuilder.Fill.HORIZONTAL, GridBagBuilder.Anchor.WEST);

        JLabel itemPath = new JLabel(item.getFolder().getFullPath() + "/" + item.getTitle());
        itemPath.setFont(new Font("Monospaced", Font.BOLD, 12));
        gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0)
                .setInsets(4, 12, 4, 0);
        put(itemPath);

        gbb.setInsets(4, 4, 4, 4);

        item.getProperties().forEach(property -> {
            gbb.setGridWidthAndWeightX(1, 0);
            JLabel label = new RightAlignedLabel(property.getDisplayName() + ":");
            label.setFont(new Font("Tahoma", Font.BOLD, 11));
            put(label);

            if (property instanceof PasswordProperty) {
                PasswordField passwordField = new PasswordField(property.toString(), false);
                passwordField.setEditable(false);
                passwordField.setBorder(BorderFactory.createEmptyBorder());
                ToggleButton showPasswordBtn = new ToggleButton(
                        IconType.EYE,
                        IconType.EYE_CLOSED,
                        false,
                        passwordField::showPassword,
                        passwordField::hidePassword
                );

                put(showPasswordBtn);
                gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
                put(passwordField);
            } else if (property instanceof LongStringProperty) {
                ScrollTextArea scrollTextArea = ((LongStringProperty) property).newComponent();
                scrollTextArea.getTextArea().setEditable(false);
                gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
                put(scrollTextArea);
            } else {
                gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
                put(new JLabel(property.toString()));
            }
        });
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }
}
