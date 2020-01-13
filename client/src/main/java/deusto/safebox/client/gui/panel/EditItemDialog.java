package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.gui.GridBagBuilder.Anchor;
import static deusto.safebox.common.gui.GridBagBuilder.Fill;

import deusto.safebox.client.datamodel.LeafItem;
import deusto.safebox.client.datamodel.property.MutableItemProperty;
import deusto.safebox.client.gui.component.PasswordField;
import deusto.safebox.client.gui.component.ToggleButton;
import deusto.safebox.client.util.IconType;
import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.gui.RightAlignedLabel;
import deusto.safebox.common.util.GuiUtil;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

/** Item modification dialog. */
public class EditItemDialog extends JDialog {

    private final GridBagBuilder gbb = new GridBagBuilder();

    private final LeafItem item;
    @SuppressWarnings("rawtypes")
    private final Map<MutableItemProperty, JComponent> components = new HashMap<>();

    // TODO: validate user input

    public EditItemDialog(JFrame owner, LeafItem item) {
        super(owner, "Entry", true);
        this.item = item;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(720, 480));
        setLayout(new GridBagLayout());

        gbb.setInsets(4, 4, 4, 4)
                .setFillAndAnchor(Fill.HORIZONTAL, Anchor.WEST);

        addItemPropertyFields();

        gbb.setWeightY(0)
                .setAnchor(Anchor.PAGE_END)
                .setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
        put(new ButtonPanel(
            () -> {
                save();
                dispose();
            },
            this::dispose,
            this::save
        ));

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }

    private void put(JComponent component) {
        add(component, gbb.getConstraints());
    }

    /** Add a field for each mutable property. */
    private void addItemPropertyFields() {
        item.getProperties().forEach(prop -> {
            if (!(prop instanceof MutableItemProperty)) {
                return;
            }

            MutableItemProperty<?, ?> property = (MutableItemProperty<?, ?>) prop;
            JComponent component = property.newComponent();
            components.put(property, component);

            gbb.setGridWidthAndWeightX(1, 0);
            put(new RightAlignedLabel(property.getDisplayName() + ":"));

            if (component instanceof PasswordField) {
                // Add the button to show or hide password.
                PasswordField passwordField = (PasswordField) component;
                ToggleButton showPasswordBtn = new ToggleButton(
                        IconType.EYE,
                        IconType.EYE_CLOSED,
                        false,
                        passwordField::showPassword,
                        passwordField::hidePassword
                );

                gbb.setWeightX(1);
                put(passwordField);
                gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 0);
                put(showPasswordBtn);
            } else {
                gbb.setGridWidthAndWeightX(GridBagConstraints.REMAINDER, 1);
                put(component);
            }
        });
    }

    // Type safe because each property is mapped to a component of the required type.
    @SuppressWarnings("unchecked")
    private void save() {
        components.forEach(MutableItemProperty::setByComponent);
        item.setLastModified(LocalDateTime.now());
    }
}
