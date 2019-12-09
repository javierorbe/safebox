package deusto.safebox.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deusto.safebox.common.util.gson.LocalDateSerializer;
import deusto.safebox.common.util.gson.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final Gson GSON;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, LocalDateSerializer.INSTANCE);
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        gsonBuilder.setPrettyPrinting();
        GSON = gsonBuilder.create();
    }

    private Constants() {
        throw new AssertionError();
    }
}
