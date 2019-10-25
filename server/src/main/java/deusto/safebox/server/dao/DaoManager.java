package deusto.safebox.server.dao;

public interface DaoManager extends AutoCloseable {

    UserDao getUserDao();
}
