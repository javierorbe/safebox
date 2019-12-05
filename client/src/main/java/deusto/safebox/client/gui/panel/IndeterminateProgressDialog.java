package deusto.safebox.client.gui.panel;

import deusto.safebox.common.util.GuiUtil;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class IndeterminateProgressDialog extends JDialog {

    public IndeterminateProgressDialog(JFrame owner, String title) {
        super(owner, title, true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar);

        pack();
        setLocation(GuiUtil.getCenteredLocation(owner, this));
    }
}
