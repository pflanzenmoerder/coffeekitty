package de.caffeine.kitty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.account.AccountRepository;
import de.caffeine.kitty.service.repository.consumtion.ConsumptionRepository;
import de.caffeine.kitty.service.repository.kitty.KittyRepository;
import de.caffeine.kitty.service.repository.user.UserRepository;


@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private KittyRepository kittyRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ConsumptionRepository consumptionRepository;
	
	public User findUserById(String userId){
		return userRepository.findOne(userId);
	}
	
	public void createUser(User user){
		userRepository.create(user);
	}
	
	public void saveUser(User user){
		userRepository.save(user);
	}
	
	public User authenticateAndGet(String email, String password) {
		return userRepository.authenticateAndGet(email, password);
	}
	
	public List<User> findUsersWithWarnLevelSet(){
		return userRepository.findUsersWithWarnLevelSet();
	}
}
