package de.caffeine.kitty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.AccountStatusEnum;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.account.AccountRepository;
import de.caffeine.kitty.service.repository.kitty.KittyRepository;
import de.caffeine.kitty.service.repository.kitty.KittySearchResult;
import de.caffeine.kitty.service.repository.user.UserRepository;

@Service
@Transactional
public class KittyService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private KittyRepository kittyRepository;
	@Autowired
	private AccountRepository accountRepository;
	
	public void saveKitty(Kitty kitty) {
		kitty = kittyRepository.save(kitty);
	}

	public Kitty findKittyById(String kittyId){
		return kittyRepository.findOne(kittyId);
	}
	
	public void createNewKitty(Kitty kitty) {
		kitty = kittyRepository.save(kitty);
		User user = userRepository.findOne(kitty.getUser().getId());
		Account account = new Account();
		account.setUser(user);
		account.setKitty(kitty);
		account.setAdmin(Boolean.TRUE);
		account.setAccountStatusEnum(AccountStatusEnum.APPROVED);
		account = accountRepository.save(account);
		user.setDefaultAccount(account);
		userRepository.save(user);
	}
	
	public KittySearchResult findKittiesbyNameAndPageSizeAndPageId(String name, int pageSize, int pageId) {
		return kittyRepository.findByNameAndPageIdWithPageSize(name, pageId, pageSize);
	}
	
}
