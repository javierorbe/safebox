package deusto.safebox.server.dao.sql;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import deusto.safebox.server.dao.UserDao;
import java.io.File;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SqLiteDaoManagerTest {

    private static final String DATABASE_FILEPATH = "./test.db";

    private static SqLiteDaoManager daoManager;

    @BeforeAll
    static void init() {
        try {
            // The database file is automatically created if it doesn't exist.
            daoManager = new SqLiteDaoManager(DATABASE_FILEPATH);
        } catch (SQLException e) {
            fail(e);
        }
    }

    @Test
    void getUserDaoTest() {
        UserDao userDao = daoManager.getUserDao();
        assertTrue(userDao instanceof SqlUserDao);
    }

    @AfterAll
    static void clean() {
        daoManager.close();

        File databaseFile = new File(DATABASE_FILEPATH);
        if (databaseFile.exists()) {
            if (!databaseFile.delete()) {
                fail("Could not delete the database file.");
            }
        }
    }
}