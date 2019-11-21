package deusto.safebox.server.dao;

import deusto.safebox.server.User;
import java.util.Optional;
import java.util.UUID;

public interface UserDao extends Dao<User, UUID> {

    /**
     * Returns an {@link Optional} with the {@link User} associated to the specified email address
     * or an empty {@code Optional} if there is no user associated to the email.
     *
     * @param email the email address.
     * @return an {@code Optional} with the user associated to the email,
     *         or an empty {@code Optional} if there is no associated user to the email.
     * @throws DaoException if there is an error getting the user.
     */
    Optional<User> getByEmail(String email) throws DaoException;
}
