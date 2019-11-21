package deusto.safebox.client.datamodel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DateTimePicker;
import javax.swing.JComponent;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ItemProperty <T>{

    private T feature;
    private String name;
    private JComponent component;

    ItemProperty(T feature, String name) {
        this.name = name;
        this.feature = feature;
        updateComponent();
    }

    public T getFeature() {
        return feature;
    }

    public void setFeature(T feature) {
        this.feature = feature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }

    private void updateComponent(){
        if (feature instanceof String || feature instanceof Folder) {
            component = new JTextField();
        } else if (feature instanceof LocalDate) {
            component = new DatePicker();
        } else if (feature instanceof LocalDateTime) {
            component = new DateTimePicker();
        }
    }
}
