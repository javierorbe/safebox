package deusto.safebox.common.util;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class JsonConfigFile implements ConfigFile {

    private JsonObject root;

    public JsonConfigFile(String path) throws FileNotFoundException {
        Gson gson = new Gson();
        root = gson.fromJson(new FileReader(path), JsonObject.class);
    }

    @Override
    public int getInt(String path) {
        return getFromPath(path).getAsInt();
    }

    @Override
    public String getString(String path) {
        return getFromPath(path).getAsString();
    }

    private JsonElement getFromPath(String path) {
        JsonElement current = root;
        String[] s = path.split("\\.");
        for (String value : s) {
            if (current instanceof JsonObject) {
                current = current.getAsJsonObject().get(value);
            }
        }
        return current;
    }
}
