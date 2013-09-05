package de.caffeine.kitty.web.user.caffeinealert;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.UserService;
import de.caffeine.kitty.web.tools.TestHelper;
import de.caffeine.kitty.web.tools.TestModel;


public class ManageCaffeineLevelAlertPanelTest {
	
	private static WicketTester wicketTester;
	
	@BeforeClass
	public static void setUp() {
		wicketTester = new WicketTester();
	}
	
	@AfterClass
	public static void tearDown() {
		wicketTester = new WicketTester();
	}
	
	@Test
	public void testRender() {
		TestModel<User> userModel = new TestModel<User>(createTestData());
		ManageCaffeineLevelAlertPanel manageCaffeineLevelAlertPanel = new ManageCaffeineLevelAlertPanel("test", userModel);
		wicketTester.startComponentInPage(manageCaffeineLevelAlertPanel);
		wicketTester.assertVisible("test");
	}
	
	@Test
	public void testAccept() throws Exception{
		TestModel<User> userModel = new TestModel<User>(createTestData());
		ManageCaffeineLevelAlertPanel manageCaffeineLevelAlertPanel = new ManageCaffeineLevelAlertPanel("test", userModel);
		UserService mockService = TestHelper.setAndGetServiceMock(manageCaffeineLevelAlertPanel, UserService.class);


		wicketTester.startComponentInPage(manageCaffeineLevelAlertPanel);
		wicketTester.assertVisible("test");
		FormTester formTester = wicketTester.newFormTester("test:alertForm");
		formTester.setValue("alert-amount", "300000");
		formTester.submit();
		verify(mockService).saveUser(userModel.getObject());
		assertEquals(Integer.valueOf(300000), userModel.getObject().getWarnLevel());
	}

	private static User createTestData() {
		User ret = new User();
		return ret; 
	}
}
