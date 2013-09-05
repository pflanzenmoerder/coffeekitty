package de.caffeine.kitty.service;

import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.AccountStatusEnum;
import de.caffeine.kitty.entities.Consumption;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.account.AccountRepository;
import de.caffeine.kitty.service.repository.consumtion.ConsumptionRepository;

@Service
@Transactional
public class AccountService {
	private static final int HOUR_DIVISOR = 1000*60*60;
	private static final int DEFAULT_CAFFEINE = 150;

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ConsumptionRepository consumptionRepository;

	public void saveAccounts(Collection<Account> accounts) {
		accountRepository.save(accounts);
	}
	
	public List<Account> findAccountRequestsByUser(User user) {
		return accountRepository.findAllRequestedAccountsByAdminUserId(user.getId());
	}
	
	public void requestAccountForKitty(User user, Kitty kitty) {
		Account account = new Account();
		account.setKitty(kitty);
		account.setUser(user);
		accountRepository.save(account);
	}
	
	public void rejectMembership(Account account) {
		accountRepository.delete(account);
	}
	
	public void acceptMembership(Account account) {
		account.setAccountStatusEnum(AccountStatusEnum.APPROVED);
		accountRepository.save(account);
	}
	
	public void drinkCoffee(Account account) {
		DateTime now = DateTime.now();
		DateTime nowPlusOneH = now.plusHours(1);
		int caffeineLevel = 0;
		for (Consumption consumption : consumptionRepository.findConsumptionsByUserIAndYoungerThan(account.getUser().getId(), now.minusHours(24).toDate())) {
			caffeineLevel += calculateCurrentLevel(consumption.getCaffeineLevel(), new Period(new DateTime(consumption.getTimeOfConsumption()), nowPlusOneH));
		}
		caffeineLevel += DEFAULT_CAFFEINE;
		
		Consumption consumption = new Consumption();
		consumption.setUser(account.getUser());
		consumption.setTimeOfConsumption(now.toDate());
		consumption.setCaffeineLevel(DEFAULT_CAFFEINE);
		consumption.setCaffeineLevelAnHourAfterConsumption(caffeineLevel);
		consumptionRepository.save(consumption);
		account.setBalance(account.getBalance()-account.getKitty().getPricePerMugInEuro());
		accountRepository.save(account);
	}
	
	private static double calculateCurrentLevel(float startValue, Period timeSpan) {
		return calculateCurrentLevel(startValue, timeSpan.getMillis()/HOUR_DIVISOR);
	}
	
	private static double calculateCurrentLevel(float startValue, float timeSpanInH) {
		if (timeSpanInH > 1) {
			return startValue * Math.pow(0.5, timeSpanInH/5);	
		}
		return startValue * timeSpanInH * Math.pow(0.5, timeSpanInH/5);
	}
	
}
