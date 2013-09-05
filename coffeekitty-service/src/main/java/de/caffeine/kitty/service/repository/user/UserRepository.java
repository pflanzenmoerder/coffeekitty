package de.caffeine.kitty.service.repository.user;

import org.springframework.data.repository.CrudRepository;

import de.caffeine.kitty.entities.User;

public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom {
}
