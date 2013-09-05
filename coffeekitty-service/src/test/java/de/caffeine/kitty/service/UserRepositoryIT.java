package de.caffeine.kitty.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.user.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:./applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, UserRepositoryIT.UserRepositoryITTestExecutionListener.class})
@Transactional
public class UserRepositoryIT {

    @Autowired
    UserRepository userRepository;
    
    @Test
	public void testFindUsersWithWarnLevelSet() {
    	Boolean found = Boolean.FALSE;
    	for (User user:userRepository.findUsersWithWarnLevelSet()) {
    		if("userRepositoryTestUser".equals(user.getFullName())) {
    			found = Boolean.TRUE;
    			break;
    		}
    	}
		assertTrue(found);
	}
    
    @Test
	public void testFindUserByUsername() {
    	assertEquals("userRepositoryTestUser", userRepository.findUserByUsername("userRepositoryTestUser").getDisplayName());
    }
    
    @Test
	public void testFindUserByEmail() {
    	assertEquals("userRepositoryTestUser@test.de", userRepository.findUserByEmail("userRepositoryTestUser@test.de").getEmail());
    }
    
    public static class UserRepositoryITTestExecutionListener extends AbstractTestExecutionListener {

		@Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            UserRepository userRepository = ((UserRepository)testContext.getApplicationContext().getBean(UserRepository.class));
            
            User user = new User();
            user.setDisplayName("userRepositoryTestUser");
            user.setFullName("userRepositoryTestUser");
            user.setPassword("wuhaaa");
            user.setSalt("aas");
            user.setWarnLevel(10);
            user.setEmail("userRepositoryTestUser@test.de");
            user = userRepository.save(user);
		}
    }
}
