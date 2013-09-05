package de.caffeine.kitty.web.user.profile;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.web.tools.TestModel;


public class ManageUserProfilePanelTest {
	
	private WicketTester wicketTester;
	
	@Before
	public void setUp() {
		wicketTester = new WicketTester();
	}
	
	@After
	public void tearDown() {
		wicketTester.destroy();
	}
	
	@Test
	public void testRender() { 
		ManageUserProfilePanel manageUserProfilePanel = new ManageUserProfilePanel("test", new TestModel<User>(createTestData()));
		wicketTester.startComponentInPage(manageUserProfilePanel);
		wicketTester.assertInvisible("test:update-feedback");
		wicketTester.assertVisible("test:update-form");
	}
	
	private static User createTestData() {
		User ret = new User();
		return ret;
	}
	
	
}
