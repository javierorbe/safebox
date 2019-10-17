package deusto.safebox.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deusto.safebox.common.json.LocalDateTimeDeserializer;
import deusto.safebox.common.json.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final Gson GSON;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        GSON = gsonBuilder.setPrettyPrinting().create();
    }

    private Constants() {
        throw new AssertionError();
    }
}
