package deusto.safebox.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** JSON formatted config file loader. */
public class JsonConfig implements ConfigFile {

    private static final Logger logger = LoggerFactory.getLogger(JsonConfig.class);

    private JsonObject root;

    public JsonConfig(String filepath) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8)) {
            root = Constants.GSON.fromJson(reader, JsonObject.class);
        }
    }

    @Override
    public int getInt(String path) {
        return getFromPath(path).getAsInt();
    }

    @Override
    public String getString(String path) {
        return getFromPath(path).getAsString();
    }

    /**
     * Returns the element in the specified JSON path.
     * The path is a string of keys separated by a period.
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
     *
     * @param path the path to the element.
     * @return the {@link JsonElement} in the path.
     */
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

    /**
     * Creates a {@link JsonConfig} from a JSON file in the system disk.
     * If the file doesn't exist in the system disk, then the resource file in {@code resourcePath}
     * is first extracted to {@code extractionPath} and then read.
     *
     * @param resourcePath file path to the JSON file in the application resources.
     * @param extractionPath file path where to read or extract the file.
     * @throws IOException if there is an error reading or extracting the file.
     */
    public static JsonConfig getOrExtractResource(String resourcePath, String extractionPath) throws IOException {
        if (!new File(extractionPath).exists()) {
            logger.trace("Config file doesn't exist, creating it.");
            extractFile(resourcePath, extractionPath);
        }
        return new JsonConfig(extractionPath);
    }

    /** Extract a resource file to disk. */
    private static void extractFile(String resourcePath, String extractionPath) throws IOException {
        try (InputStream is = JsonConfig.class.getResourceAsStream(resourcePath);
             FileOutputStream fos = new FileOutputStream(extractionPath)) {
            byte[] buffer = new byte[2048];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
