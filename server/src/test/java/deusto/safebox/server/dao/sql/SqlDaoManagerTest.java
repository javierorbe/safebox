package deusto.safebox.server.dao.sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

import deusto.safebox.server.dao.ItemCollectionDao;
import deusto.safebox.server.dao.UserDao;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SqlDaoManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(SqlDaoManagerTest.class);

    private static final Path TEST_DATABASE = Path.of("./test.db");

    private static SqlDaoManager daoManager;

    @BeforeAll
    static void setup() {
        // The database file is automatically created if it doesn't exist.
        daoManager = SqlDaoManager.ofSqlite(TEST_DATABASE);
    }

    @Test
    void getUserDaoTest() {
        UserDao userDao = daoManager.getUserDao();
        assertTrue(userDao instanceof SqlUserDao);
    }

    @Test
    void getItemCollectionDaoTest() {
        ItemCollectionDao itemCollectionDao = daoManager.getItemCollectionDao();
        assertTrue(itemCollectionDao instanceof SqlItemCollectionDao);
    }

    @AfterAll
    static void clean() {
        daoManager.close();

        try {
            Files.deleteIfExists(TEST_DATABASE);
        } catch (IOException e) {
            logger.error("Could not delete the database file.", e);
        }
    }
}
