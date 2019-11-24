package deusto.safebox.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON formatted config file.
 *
 * <p>The path used in some of the methods is a string of keys separated by a period.
 *
 * <p>For example, the path to access the street attribute in this JSON object is <code>address.street</code>
 * <pre>
 * {
 *  name: "John",
 *  age: 31,
 *  address: {
 *      street: "Sesame Street"
 *  }
 * }
 * </pre>
 */
public class JsonConfig implements ConfigFile {

    private static final Logger logger = LoggerFactory.getLogger(JsonConfig.class);

    private final Path file;
    private JsonObject root;

    JsonConfig(Path file) throws IOException {
        this.file = file;
        root = Constants.GSON.fromJson(Files.newBufferedReader(file, StandardCharsets.UTF_8), JsonObject.class);
    }

    @Override
    public int getInt(String path) {
        return elementAtPath(path).getAsInt();
    }

    @Override
    public void setInt(int value, String path) {
        String[] s = path.split("\\.");
        getObjectAt(s).addProperty(s[s.length - 1], value);
    }

    @Override
    public String getString(String path) {
        return elementAtPath(path).getAsString();
    }

    @Override
    public void setString(String value, String path) {
        String[] s = path.split("\\.");
        getObjectAt(s).addProperty(s[s.length - 1], value);
    }

    private JsonObject getObjectAt(String[] path) {
        JsonObject current = root;
        for (int i = 0; i < path.length - 1; i++) {
            current = current.getAsJsonObject(path[i]);
        }
        return current;
    }

    public Map<String, String> getStringMap(String path) {
        Map<String, String> stringMap = new HashMap<>();
        JsonElement object = objectAtPath(path);
        object.getAsJsonObject().entrySet().forEach(entry -> {
            String key = entry.getKey();
            String string = entry.getValue().getAsString();
            stringMap.put(key, string);
        });
        return stringMap;
    }

    @Override
    public void save() {
        try {
            Constants.GSON.toJson(root, Files.newBufferedWriter(file));
        } catch (IOException e) {
            logger.error("Error saving JSON config file.", e);
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }

    /**
     * Returns the element in the specified JSON path.
     *
     * @param path the path to the element.
     * @return the {@link JsonElement} in the path.
     */
    private JsonElement elementAtPath(String path) {
        JsonElement current = root;
        String[] s = path.split("\\.");
        for (String value : s) {
            if (current instanceof JsonObject) {
                current = current.getAsJsonObject().get(value);
            }
        }
        return current;
    }

    private JsonObject objectAtPath(String path) {
        JsonObject current = root;
        String[] s = path.split("\\.");
        if (s.length > 1) {
            for (String value : s) {
                current = current.get(value).getAsJsonObject();
            }
        }
        return current;
    }

    /**
     * Creates a {@link JsonConfig} from a JSON file in the system disk.
     * If the file doesn't exist in the system disk, then the resource file in {@code resourcePath}
     * is first extracted to {@code extractionPath} and then read.
     *
     * @param resourcePath file path to the JSON file in the application resources.
     * @param extract file path where to read or extract the file.
     * @return the loaded JSON config file.
     * @throws IOException if there is an error reading or extracting the file.
     */
    public static JsonConfig ofResource(String resourcePath, Path extract) throws IOException {
        if (!Files.exists(extract)) {
            logger.trace("Config file doesn't exist, creating it.");
            try (InputStream is = JsonConfig.class.getResourceAsStream("/" + resourcePath)) {
                Files.copy(is, extract);
            }
        }
        return new JsonConfig(extract);
    }
}
