package deusto.safebox.server.dao;

/** Maintains the used DAOs. */
public interface DaoManager {

    UserDao getUserDao();

    ItemCollectionDao getItemCollectionDao();
}
