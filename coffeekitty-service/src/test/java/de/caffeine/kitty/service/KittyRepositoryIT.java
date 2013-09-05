package de.caffeine.kitty.service;

import static org.junit.Assert.assertEquals;

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

import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.kitty.KittyRepository;
import de.caffeine.kitty.service.repository.kitty.KittySearchResult;
import de.caffeine.kitty.service.repository.user.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:./applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, KittyRepositoryIT.KittyRepositoryITTestExecutionListener.class})
@Transactional
public class KittyRepositoryIT {
	
    @Autowired
    KittyRepository kittyRepository;
    
    @Test
    public void testfindByNameAndPageIdWithPageSize() {
    	KittySearchResult kittySearchResult = kittyRepository.findByNameAndPageIdWithPageSize("kittyRe", 0, 10);
        assertEquals(Long.valueOf(30), kittySearchResult.total);
        assertEquals(10, kittySearchResult.kitties.size());
    }
    
    public static class KittyRepositoryITTestExecutionListener extends AbstractTestExecutionListener {

		@Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            UserRepository userRepository = ((UserRepository)testContext.getApplicationContext().getBean(UserRepository.class));
            KittyRepository kittyRepository = ((KittyRepository)testContext.getApplicationContext().getBean(KittyRepository.class));;
            
	        User user = new User();
	        user.setDisplayName("kittyRepositoryTestUser");
	        user.setFullName("kittyRepositoryTestUser");
	        user.setPassword("wuhaaa");
	        user.setSalt("aas");
	        user.setEmail("kittyRepositoryTestUser@test.de");
	        user = userRepository.save(user);
	        for (int i =0; i < 30; i++) {
	        	Kitty kitty = new Kitty();
	        	kitty.setUser(user);
	        	kitty.setName("kittyRepo"+i);
	        	kittyRepository.save(kitty);	
	        }
	        
		}
    }
}
