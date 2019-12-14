package deusto.safebox.client.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PassphraseGenerator {
    private static final URL WORD_LIST_PATH = PassphraseGenerator.class.getResource(
            "/wordlist/" + "eff_short_wordlist_1.txt");
    private static final List<String> WORDS = updateWords();

    private static final Random RANDOM = ThreadLocalRandom.current();

    public static String generatePassphrase(int passLength, String separator) {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passLength - 1; i++) {
            password.append(WORDS.get(RANDOM.nextInt(WORDS.size())));
            password.append(separator);
        }
        password.append(WORDS.get(RANDOM.nextInt(WORDS.size())));

        return password.toString();
    }

    public static List<String> updateWords() {
        try {
            return Files.readAllLines(Paths.get(WORD_LIST_PATH.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("File couldn't be read", e);
        }
    }
}
