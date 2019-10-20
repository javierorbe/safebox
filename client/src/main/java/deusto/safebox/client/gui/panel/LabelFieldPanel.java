package deusto.safebox.client.gui.panel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LabelFieldPanel extends JPanel {

    private JLabel label;
    private JTextField textField;

    public LabelFieldPanel() {
        label = new JLabel();
        textField = new JTextField();
    }

    public LabelFieldPanel(String textLabel, int columns) {
        label = new JLabel(textLabel);
        textField = new JTextField(columns);
    }

    public LabelFieldPanel(String textLabel, int columns, boolean isPassword) {
        label = new JLabel(textLabel);
        if(isPassword){
            textField = new JPasswordField(columns);
        }
        else{
            textField = new JTextField(columns);
        }
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }
}
