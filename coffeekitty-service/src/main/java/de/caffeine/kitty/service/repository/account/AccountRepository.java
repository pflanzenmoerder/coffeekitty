package de.caffeine.kitty.service.repository.account;

import org.springframework.data.repository.CrudRepository;

import de.caffeine.kitty.entities.Account;


public interface AccountRepository extends CrudRepository<Account, String>, AccountRepositoryCustom{
}
