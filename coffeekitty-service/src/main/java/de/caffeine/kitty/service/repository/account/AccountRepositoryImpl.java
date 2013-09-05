package de.caffeine.kitty.service.repository.account;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;
import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.AccountStatusEnum;
import de.caffeine.kitty.entities.User;

import de.caffeine.kitty.entities.QAccount;
import de.caffeine.kitty.entities.QUser;

public class AccountRepositoryImpl implements AccountRepositoryCustom{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Account> findAllRequestedAccountsByAdminUserId(String userId) {
		JPAQuery query = new JPAQuery(em);
		User user = query.from(QUser.user).where(QUser.user.id.eq(userId)).leftJoin(QUser.user.kitties).fetch().uniqueResult(QUser.user);
		query = new JPAQuery(em);
		if(!user.getKitties().isEmpty()) {
			return query.from(QAccount.account).where(QAccount.account.kitty.in(user.getKitties())).where(QAccount.account.accountStatusEnum.eq(AccountStatusEnum.REQUESTED)).list(QAccount.account);	
		}
		return Collections.emptyList();
	}
}
