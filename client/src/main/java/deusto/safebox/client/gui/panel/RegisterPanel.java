package deusto.safebox.client.gui.panel;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.*;

public class RegisterPanel extends JPanel {

    private SpringLayout sLayout = new SpringLayout();
    private static String[] nameLabels = {"Name:", "Username:", "Password:", "Repeat password:"};
    private final int N = nameLabels.length;
    private Label[] labels = new Label[N];
    private JTextField[] textFields = new JTextField[N];
    private JPanel[] content = new JPanel[N];

    private static int WIDTH_WINDOW = 500;
    private static int HEIGHT_WINDOW = 450;
    private static int SHIFT_X = WIDTH_WINDOW/10;
    private static int SHIFT_Y = HEIGHT_WINDOW/10;

    public RegisterPanel() {
        setLayout(sLayout);
        addLabelsTextFields();

        JButton button = new JButton("Register");

        sLayout.putConstraint(SpringLayout.WEST, button, 3* SHIFT_X, SpringLayout.WEST, this);
        sLayout.putConstraint(SpringLayout.EAST, button, 3*-SHIFT_X, SpringLayout.EAST, this);
        sLayout.putConstraint(SpringLayout.NORTH, button, (N+2)* SHIFT_Y, SpringLayout.NORTH, this);
        add(button);
    }

    private void addLabelsTextFields(){
        for (int i = 0; i < N; i++) {
            labels[i] = new Label(nameLabels[i], Label.CENTER);
            textFields[i] = new JTextField(20);

            content[i] = new JPanel(new FlowLayout());
            content[i].add(labels[i]);
            content[i].add(textFields[i]);
            labels[i].setPreferredSize(new Dimension(120,35));

            textFields[i].setHorizontalAlignment(JTextField.CENTER);

            sLayout.putConstraint(SpringLayout.WEST, content[i], SHIFT_X, SpringLayout.WEST, this);
            sLayout.putConstraint(SpringLayout.EAST, content[i], -SHIFT_X, SpringLayout.EAST, this);
            sLayout.putConstraint(SpringLayout.NORTH, content[i], (i+1)* SHIFT_Y, SpringLayout.NORTH, this);

            add(content[i]);
        }

    }
}
