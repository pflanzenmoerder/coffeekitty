package de.caffeine.kitty.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
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

import de.caffeine.kitty.entities.Consumption;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.consumtion.ConsumptionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:./applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, UserServiceTest.UserServiceITTestExecutionListener.class})
@Transactional
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CaffeinieService caffeinieService;
	
	@Autowired
	private ConsumptionRepository consumptionRepository;
	
	private static String userId;
	
	@Test
	public void testDrinkCoffee() {
		User user = userService.findUserById(userId);
		accountService.drinkCoffee(user.getDefaultAccount());
		user = userService.findUserById(userId);
		accountService.drinkCoffee(user.getDefaultAccount());
		user = userService.findUserById(userId);
		accountService.drinkCoffee(user.getDefaultAccount());
		Date now = new Date();
		assertEquals(3, consumptionRepository.findConsumptionsByUserIdAndTimeRange(userId, new Date(now.getTime()-10000), now).size());
		Consumption recent = consumptionRepository.findMostRecentConsumptionByUserId(userId);
		assertNotSame(recent.getCaffeineLevel(), recent.getCaffeineLevelAnHourAfterConsumption());
	}
	
	@Test
    @Ignore
	public void testGetCaffeineDTOsForLast7DaysByUser() {
		User user = userService.findUserById(userId);
		accountService.drinkCoffee(user.getDefaultAccount());
		user = userService.findUserById(userId);
		accountService.drinkCoffee(user.getDefaultAccount());
		user = userService.findUserById(userId);
		accountService.drinkCoffee(user.getDefaultAccount());
		List<CaffeineDTO> caffeineDTOs = caffeinieService.getCaffeineDTOsForLast7DaysByUser(user);
        assertEquals(43, (long) caffeineDTOs.get(caffeineDTOs.size() - 1).level);
	}
	
	public static class UserServiceITTestExecutionListener extends AbstractTestExecutionListener {

		@Override
        public void beforeTestClass(TestContext testContext) throws Exception {
			UserService userService = ((UserService)testContext.getApplicationContext().getBean(UserService.class));
            KittyService kittyService = ((KittyService)testContext.getApplicationContext().getBean(KittyService.class));
			
	        User user = new User();
	        userId = user.getId();
	        user.setDisplayName("userServiceTestUser");
	        user.setFullName("userServiceTestUser");
	        user.setPassword("wuhaaa");
	        user.setSalt("aas");
	        user.setEmail("userServiceTestUser@test.de");
	        userService.createUser(user);
        	Kitty kitty = new Kitty();
        	kitty.setUser(user);
        	kitty.setName("kittyUserServiceRepo");
        	kittyService.createNewKitty(kitty);
	        
		}
    }
}
