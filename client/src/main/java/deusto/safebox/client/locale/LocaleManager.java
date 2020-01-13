package deusto.safebox.client.locale;

import deusto.safebox.common.util.JsonConfig;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocaleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleManager.class);

    private static final Map<Message, String> MESSAGES = new EnumMap<>(Message.class);

    public static void loadTranslation(Language language) throws IOException, URISyntaxException {
        String langFileName = String.format("/lang/%s.json", language.getCode());
        Path langFile = Path.of(LocaleManager.class.getResource(langFileName).toURI());
        loadFromFile(langFile);
    }

    private static void loadFromFile(Path file) throws IOException {
        JsonConfig translationFile = new JsonConfig(file);
        Map<String, String> stringMap = translationFile.getStringMap("");
        stringMap.forEach((key, translation) -> {
            String messageKey = key.toUpperCase();
            try {
                Message message = Message.valueOf(messageKey);
                MESSAGES.put(message, translation);
            } catch (IllegalArgumentException ignored) {
                LOGGER.warn("Invalid message key ({}).", messageKey);
            }
        });
    }

    static Optional<String> getTranslation(Message message) {
        return Optional.ofNullable(MESSAGES.get(message));
    }

    private LocaleManager() {
        throw new AssertionError();
    }
}
