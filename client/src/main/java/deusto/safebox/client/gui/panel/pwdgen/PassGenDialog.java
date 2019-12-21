package deusto.safebox.client.gui.panel.pwdgen;

import deusto.safebox.common.gui.GridBagBuilder;
import deusto.safebox.common.util.GuiUtil;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GridBagLayout;

public class PassGenDialog extends JDialog {

    public PassGenDialog(JFrame owner) {
        super(owner, "Password Generator", true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(700,500));
        setLayout(new GridBagLayout());

        PassGenPanel passGenerator = new PassGenPanel(this);

        GridBagBuilder gbb = new GridBagBuilder();
        gbb.setFillAndAnchor(GridBagBuilder.Fill.BOTH, GridBagBuilder.Anchor.CENTER);
        gbb.setGridWidthAndWeightX(1,1);
        add(passGenerator, gbb.getConstraints());

        pack();
        setLocation(GuiUtil.getCenteredLocation(this));
        setVisible(true);
    }
}