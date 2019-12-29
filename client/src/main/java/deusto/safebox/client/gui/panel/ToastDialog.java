package deusto.safebox.client.gui.panel;

import static deusto.safebox.common.util.GuiUtil.runSwing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * A message dialog inspired by Android toasts.
 *
 * @see <a href="https://developer.android.com/guide/topics/ui/notifiers/toasts">Toasts - Android</a>
 */
class ToastDialog extends JDialog {

    private ScheduledFuture<?> opacityReducer;
    private float opacity = 1;

    /**
     * Constructs a toast message dialog.
     *
     * @param message the displayed message.
     * @param backgroundColor the background color of the dialog.
     * @param time the seconds the dialog is shown at full opacity.
     * @param parent the component that creates the dialog.
     *                        It is used to calculate the location of the dialog.
     */
    private ToastDialog(JComponent parent, Color backgroundColor, int time, String message) {
        setFocusable(false);
        setAutoRequestFocus(false);
        setUndecorated(true);
        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel toastLabel = new JLabel(message);
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        toastLabel.setForeground(Color.WHITE);
        panel.add(toastLabel);

        pack();

        // Set the location of the dialog south of the parent component.
        Point location = parent.getLocationOnScreen();
        location.x += parent.getWidth() / 2 - getWidth() / 2;
        location.y += parent.getHeight() - getHeight() * 2;
        setLocation(location);

        setVisible(true);

        opacityReducer = Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(() -> {
                    if (opacity < 0.2) {
                        opacityReducer.cancel(false);
                        dispose();
                    } else {
                        opacity -= 0.15;
                        setOpacity(opacity);
                    }
                }, time * 1000, 100, TimeUnit.MILLISECONDS);
    }

    public static void showError(JComponent parent, String message) {
        runSwing(() -> new ToastDialog(parent, Color.RED, 3, message));
    }

    public static void showInfo(JComponent parent, String message) {
        runSwing(() -> new ToastDialog(parent, Color.GREEN, 2, message));
    }
}
