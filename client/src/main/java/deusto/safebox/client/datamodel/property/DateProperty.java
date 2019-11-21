package deusto.safebox.client.datamodel.property;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import deusto.safebox.common.util.Constants;
import java.time.LocalDate;

public class DateProperty extends MutableItemProperty<LocalDate, DatePicker> {

    public DateProperty(String displayName, LocalDate initialValue) {
        super(displayName, initialValue);
    }

    @Override
    public DatePicker newComponent() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setAllowKeyboardEditing(false);
        DatePicker datePicker = new DatePicker(settings);
        datePicker.setDate(get());
        return datePicker;
    }

    @Override
    public void setByComponent(DatePicker component) {
        set(component.getDate());
    }

    @Override
    public String toString() {
        return get() == null ? "Never" : Constants.DATE_FORMATTER.format(get());
    }
}
