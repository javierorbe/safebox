package deusto.safebox.client.gui.panel;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private SpringLayout sLayout = new SpringLayout();
    private String[] nameLabels = {"Username:", "Password:"};
    private final int N = nameLabels.length;
    private JLabel[] labels = new JLabel[N];
    private JTextField[] textFields = new JTextField[N];
    private JPanel[] content = new JPanel[N];

    private static int WIDTH_WINDOW = 500;
    private static int HEIGHT_WINDOW = 450;
    private static int SHIFT_X = WIDTH_WINDOW/10;
    private static int SHIFT_Y = HEIGHT_WINDOW/10;

    public LoginPanel() {
        setLayout(sLayout);
        addLabelsTextFields();

        JButton button = new JButton("Login");

        sLayout.putConstraint(SpringLayout.WEST, button, 3* SHIFT_X, SpringLayout.WEST, this);
        sLayout.putConstraint(SpringLayout.EAST, button, 3*-SHIFT_X, SpringLayout.EAST, this);
        sLayout.putConstraint(SpringLayout.NORTH, button, (N+2)* SHIFT_Y, SpringLayout.NORTH, this);

        JCheckBox rememberUsename = new JCheckBox("Remember username");
        sLayout.putConstraint(SpringLayout.WEST, rememberUsename,  2*SHIFT_X, SpringLayout.WEST, this);
        sLayout.putConstraint(SpringLayout.EAST, rememberUsename, 2*-SHIFT_X, SpringLayout.EAST, this);
        sLayout.putConstraint(SpringLayout.NORTH, rememberUsename, (N+1)* SHIFT_Y, SpringLayout.NORTH, this);

        //TODO: Añadir funcionalidad a todo
        System.out.println("getWidth: "+labels[0].getWidth());
        System.out.println("getPreferedSize: "+labels[0].getPreferredSize());
        System.out.println("getSize: "+labels[0].getSize());
        add(rememberUsename);
        add(button);
    }

    private void addLabelsTextFields(){
        for (int i = 0; i < N; i++) {
            labels[i] = new JLabel(nameLabels[i], JLabel.CENTER);
            textFields[i] = new JTextField(20);

            content[i] = new JPanel(new FlowLayout());
            content[i].add(labels[i]);
            content[i].add(textFields[i]);
            labels[i].setPreferredSize(new Dimension(30,35));

            textFields[i].setHorizontalAlignment(JTextField.CENTER);

            sLayout.putConstraint(SpringLayout.WEST, content[i], SHIFT_X, SpringLayout.WEST, this);
            sLayout.putConstraint(SpringLayout.EAST, content[i], -SHIFT_X, SpringLayout.EAST, this);
            sLayout.putConstraint(SpringLayout.NORTH, content[i], (i+1)* SHIFT_Y, SpringLayout.NORTH, this);

            add(content[i]);
        }
    }

}
