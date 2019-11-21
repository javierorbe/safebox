package deusto.safebox.client.datamodel.property;

import deusto.safebox.common.util.Constants;
import java.time.LocalDateTime;

public class DateTimeProperty extends ItemProperty<LocalDateTime> {

    public DateTimeProperty(String displayName, LocalDateTime initialValue) {
        super(displayName, initialValue);
    }

    @Override
    public String toString() {
        return Constants.DATE_TIME_FORMATTER.format(get());
    }
}
