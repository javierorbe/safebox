package deusto.safebox.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

class ToastDialog extends JDialog {

    /**
     * Constructs a toast message dialog.
     *
     * @param message the displayed message.
     * @param backgroundColor the background color of the dialog.
     * @param time the seconds the dialog is shown at full opacity.
     * @param parentComponent the component that creates the dialog.
     *                        It is used to calculate the location of the dialog.
     */
    ToastDialog(String message, Color backgroundColor, int time, JComponent parentComponent) {
        setFocusable(false);
        setAutoRequestFocus(false);
        setUndecorated(true);
        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        // panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.setBackground(backgroundColor);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel toastLabel = new JLabel(message);
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        toastLabel.setForeground(Color.WHITE);
        panel.add(toastLabel);

        pack();

        Point location = parentComponent.getLocationOnScreen();
        location.x += parentComponent.getWidth() / 2 - getWidth() / 2;
        location.y += parentComponent.getHeight() - getHeight() * 2;
        setLocation(location);

        setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(time * 1000);
                for (double d = 1; d > 0.2; d -= 0.15) {
                    Thread.sleep(100);
                    setOpacity((float) d);
                }
                dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
