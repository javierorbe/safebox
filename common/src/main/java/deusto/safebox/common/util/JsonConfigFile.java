package deusto.safebox.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonConfigFile implements ConfigFile {

    private JsonObject root;

    /**
     * Creates a JSON file reader.
     *
     * @param path file path to the JSON file.
     * @throws FileNotFoundException if the JSON file is not found.
     */
    public JsonConfigFile(String path) throws FileNotFoundException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        root = gson.fromJson(reader, JsonObject.class);
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
