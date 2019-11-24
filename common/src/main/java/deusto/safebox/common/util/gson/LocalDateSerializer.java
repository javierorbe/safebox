package deusto.safebox.common.util.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import deusto.safebox.common.util.Constants;
import java.lang.reflect.Type;
import java.time.LocalDate;

public enum LocalDateSerializer implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    INSTANCE;

    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(Constants.DATE_FORMATTER.format(localDate));
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDate.parse(json.getAsString(), Constants.DATE_FORMATTER);
    }
}
