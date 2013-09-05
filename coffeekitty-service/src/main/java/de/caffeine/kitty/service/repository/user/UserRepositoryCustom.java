package de.caffeine.kitty.service.repository.user;

import java.util.List;

import de.caffeine.kitty.entities.User;

public interface UserRepositoryCustom {
	List<User> findUsersWithWarnLevelSet();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User authenticateAndGet(String username, String password);
    void create(User user);
}
