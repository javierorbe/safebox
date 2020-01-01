package deusto.safebox.client.gui.panel.pwdgen;

import java.util.function.IntConsumer;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * Linked {@link JSlider} and {@link JSpinner} to have the same value.
 */
class SliderAndSpinner {

    private final JSlider slider;
    private final JSpinner spinner;

    SliderAndSpinner(int min, int max, int initial, IntConsumer update) {
        slider = new JSlider(SwingConstants.HORIZONTAL, min, max, initial);
        slider.setFocusable(false);

        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(initial, min, max, 1);
        spinner = new JSpinner(spinnerNumberModel);
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) spinner.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);

        slider.addChangeListener(e -> {
            int value = slider.getValue();
            spinner.setValue(value);
            update.accept(value);
        });

        spinner.addChangeListener(e -> {
            int value = (int) spinner.getValue();
            slider.setValue(value);
            update.accept(value);
        });
    }

    public JSlider getSlider() {
        return slider;
    }

    public JSpinner getSpinner() {
        return spinner;
    }
}
