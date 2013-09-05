package de.caffeine.kitty.service;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.AccountStatusEnum;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.account.AccountRepository;
import de.caffeine.kitty.service.repository.kitty.KittyRepository;
import de.caffeine.kitty.service.repository.user.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:./applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, AccountRepositoryIT.AccountRepositoryITTestExecutionListener.class})
@Transactional
public class AccountRepositoryIT {
	
	private static String userId;
	
    @Autowired
    AccountRepository accountRepository;
    
    @Test
    public void testFindAllRequestedAccountsByAdminUserId() {
        assertEquals(1, accountRepository.findAllRequestedAccountsByAdminUserId(userId).size());
    }
    
    public static class AccountRepositoryITTestExecutionListener extends AbstractTestExecutionListener {

		@Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            UserRepository userRepository = ((UserRepository)testContext.getApplicationContext().getBean(UserRepository.class));
            KittyRepository kittyRepository = ((KittyRepository)testContext.getApplicationContext().getBean(KittyRepository.class));;
            
	        User user = new User();
	        userId = user.getId();
	        user.setDisplayName("accountRepositoryTestUser");
	        user.setFullName("accountRepositoryTestUser");
	        user.setPassword("wuhaaa");
	        user.setSalt("aas");
	        user.setEmail("accountRepositoryTestUser@test.de");
	        user = userRepository.save(user);
	        
	        Kitty kitty = new Kitty();
	        kitty.setName("accountRepositoryKitty");
	        kitty.setUser(user);
	        kitty = kittyRepository.save(kitty);
	        
	        Account acc = new Account();
	        acc.setUser(user);
	        acc.setKitty(kitty);
	        acc.setAccountStatusEnum(AccountStatusEnum.APPROVED);
	        acc.setAdmin(true);
	       
	        kitty.addAccount(acc);
	        kitty = kittyRepository.save(kitty);
	        
	        user = new User();
	        user.setDisplayName("accountRepositoryTestUser2");
	        user.setFullName("accountRepositoryTestUser2");
	        user.setPassword("wuhaaa");
	        user.setSalt("aas");
	        user.setEmail("accountRepositoryTestUser2@test.de");
	        user = userRepository.save(user);
	        
	        acc = new Account();
	        acc.setUser(user);
	        acc.setKitty(kitty);
	        kitty.addAccount(acc);
	        
	        kittyRepository.save(kitty);
		}
    }
}
