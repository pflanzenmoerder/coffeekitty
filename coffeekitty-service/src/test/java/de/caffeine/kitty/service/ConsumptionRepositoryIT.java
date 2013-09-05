package de.caffeine.kitty.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

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
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.consumtion.ConsumptionRepository;
import de.caffeine.kitty.service.repository.consumtion.HiscoreDTO;
import de.caffeine.kitty.service.repository.user.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:./applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, ConsumptionRepositoryIT.AccountRepositoryITTestExecutionListener.class})
@Transactional
public class ConsumptionRepositoryIT {
	
	private static String userId;
	private static Date time;
	
    @Autowired
    ConsumptionRepository consumptionRepository;
    
    @Test
    public void testFindMostRecentConsumptionByUserId() {
        assertEquals(time, consumptionRepository.findMostRecentConsumptionByUserId(userId).getTimeOfConsumption());
    }
    
    @Test
    public void testFindConsumptionsByUserIAndYoungerThan() {
    	assertEquals(40, consumptionRepository.findConsumptionsByUserIAndYoungerThan(userId, new Date(time.getTime()-(360000*41))).size());
    }
    
    @Test
    public void testfindConsumptionsByUserIdAndTimeRange() {
    	Date earlier = new Date(time.getTime()-(360000*41));
    	assertEquals(40, consumptionRepository.findConsumptionsByUserIdAndTimeRange(userId, earlier, time).size());
    }
    
    @Test
    public void testFindConsumptionsWithTopCaffeineByTimeRange() {
    	Date earlier = new Date(time.getTime()-10000);
    	List<HiscoreDTO> res = consumptionRepository.findConsumptionsWithTopCaffeineByTimeRange(10, earlier, time);
    	assertEquals(2, res.size());
    }
    
    @Test
    public void testFindTopConsumptionsAllTime() {
    	List<HiscoreDTO> res = consumptionRepository.findTopConsumptionsAllTime(10);
    	assertEquals(2, res.size());
    }
    
    public static class AccountRepositoryITTestExecutionListener extends AbstractTestExecutionListener {

		@Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            UserRepository userRepository = ((UserRepository)testContext.getApplicationContext().getBean(UserRepository.class));
            ConsumptionRepository consumptionRepository = ((ConsumptionRepository)testContext.getApplicationContext().getBean(ConsumptionRepository.class));
	        User user = new User();
	        userId = user.getId();
	        user.setDisplayName("consumptionRepositoryTestUser");
	        user.setFullName("consumptionRepositoryTestUser");
	        user.setPassword("wuhaaa");
	        user.setSalt("aas");
	        user.setEmail("consumptionRepositoryTestUser@test.de");
	        user.setWarnLevel(20);
	        user = userRepository.save(user);
	        
	        time = new Date();
	        long timestamp = time.getTime();
	        for (int i = 0; i < 40; i++) {
	        	Consumption consumption = new Consumption();
	        	consumption.setCaffeineLevel(10+i);
	        	consumption.setTimeOfConsumption(new Date(timestamp - i*360000));
	        	consumption.setCaffeineLevelAnHourAfterConsumption(10+i);
	        	consumption.setUser(user);
	        	consumptionRepository.save(consumption);
	        }
	        
	        user = new User();
	        user.setDisplayName("consumptionRepositoryTestUser2");
	        user.setFullName("consumptionRepositoryTestUser2");
	        user.setPassword("wuhaaa");
	        user.setSalt("aas");
	        user.setEmail("consumptionRepositoryTestUser2@test.de");
	        user = userRepository.save(user);
	        timestamp = time.getTime();
	        for (int i = 0; i < 41; i++) {
	        	Consumption consumption = new Consumption();
	        	consumption.setCaffeineLevel(10+i);
	        	consumption.setCaffeineLevelAnHourAfterConsumption(10+i);
	        	consumption.setTimeOfConsumption(new Date(timestamp - i*360000));
	        	consumption.setUser(user);
	        	consumptionRepository.save(consumption);
	        }
		}
    }
}
