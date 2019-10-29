package deusto.safebox.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JsonConfigTest {

    private static final String TEST_CONFIG_FILEPATH = "./test_config.json";
    private static final String TEST_CONTENT = "{ val1: \"example\", val2: { val3: 1337 } }";

    private static ConfigFile configFile;

    @BeforeAll
    static void setup() {
        try {
            createTestConfig();
        } catch (FileNotFoundException e) {
            fail(e);
        }

        try {
            configFile = new JsonConfig(TEST_CONFIG_FILEPATH);
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
        File file = new File(TEST_CONFIG_FILEPATH);
        if (file.exists()) {
            if (!file.delete()) {
                fail("Could not delete test config file.");
            }
        }
    }

    private static void createTestConfig() throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(TEST_CONFIG_FILEPATH)) {
            pw.println(TEST_CONTENT);
        }
    }
}