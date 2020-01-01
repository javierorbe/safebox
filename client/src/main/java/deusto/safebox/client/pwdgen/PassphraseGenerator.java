package deusto.safebox.client.pwdgen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PassphraseGenerator {

    private static final Random RANDOM = ThreadLocalRandom.current();
    private static final String WORD_LIST_FILENAME = "eff_short_wordlist_1";
    private static final List<String> WORD_LIST = readWordList(String.format("/wordlist/%s.txt", WORD_LIST_FILENAME));

    public static String generate(int wordCount, String separator) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < wordCount; i++) {
            builder.append(WORD_LIST.get(RANDOM.nextInt(WORD_LIST.size())));
            if (i + 1 < wordCount) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }

    private static List<String> readWordList(String resourceName) {
        try {
            URL url = PassphraseGenerator.class.getResource(resourceName);
            Path file = Path.of(url.toURI());
            return Files.readAllLines(file);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Could not load the word list.", e);
        }
    }

    private PassphraseGenerator() {
        throw new AssertionError();
    }
}
