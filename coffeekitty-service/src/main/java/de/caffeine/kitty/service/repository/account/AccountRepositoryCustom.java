package de.caffeine.kitty.service.repository.account;

import java.util.List;

import de.caffeine.kitty.entities.Account;


public interface AccountRepositoryCustom {
	List<Account> findAllRequestedAccountsByAdminUserId(String userId);
}
