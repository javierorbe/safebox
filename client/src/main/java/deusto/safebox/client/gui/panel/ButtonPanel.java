package deusto.safebox.client.gui.panel;

import deusto.safebox.client.locale.Message;
import deusto.safebox.common.gui.SimpleButton;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Button container panel.
 * Contains three buttons: OK, Cancel and Apply.
 */
class ButtonPanel extends JPanel {

    private final JButton applyBtn;

    ButtonPanel(Runnable acceptAction, Runnable cancelAction, Runnable applyAction) {
        super(new FlowLayout(FlowLayout.RIGHT));

        JButton acceptBtn = new SimpleButton("OK", acceptAction);
        JButton cancelBtn = new SimpleButton(Message.CANCEL.get(), cancelAction);
        applyBtn = new SimpleButton(Message.APPLY.get(), applyAction);

        add(acceptBtn);
        add(cancelBtn);
        add(applyBtn);
    }

    /** Enables the apply button. */
    public void enableApply() {
        applyBtn.setEnabled(true);
    }

    /** Disables the apply button. */
    public void disableApply() {
        applyBtn.setEnabled(false);
    }
}
