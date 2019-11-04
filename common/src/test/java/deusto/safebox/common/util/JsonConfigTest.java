package deusto.safebox.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JsonConfigTest {

    private static final Path TEST_CONFIG = Path.of("./test_config.json");
    private static final String TEST_CONTENT = "{ val1: \"example\", val2: { val3: 1337 } }";

    private static ConfigFile configFile;

    @BeforeAll
    static void setup() {
        try {
            Files.writeString(TEST_CONFIG, TEST_CONTENT);
        } catch (IOException e) {
            fail("Could not write to the test config file.", e);
        }

        try {
            configFile = new JsonConfig(TEST_CONFIG);
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void getStringTest() {
        String val1 = configFile.getString("val1");
        assertEquals("example", val1);
    }

    @Test
    void getIntTest() {
        int val3 = configFile.getInt("val2.val3");
        assertEquals(1337, val3);
    }

    @AfterAll
    static void clean() {
        try {
            Files.deleteIfExists(TEST_CONFIG);
        } catch (IOException e) {
            fail("Could not delete the config file.", e);
        }
    }
}
