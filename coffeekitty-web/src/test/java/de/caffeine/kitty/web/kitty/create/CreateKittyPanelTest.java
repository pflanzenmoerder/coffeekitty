package de.caffeine.kitty.web.kitty.create;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.KittyService;
import de.caffeine.kitty.web.tools.TestHelper;
import de.caffeine.kitty.web.tools.TestModel;


public class CreateKittyPanelTest {
	
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
		CreateKittyPanel createKittyPanel = new CreateKittyPanel("test", userModel);
		wicketTester.startComponentInPage(createKittyPanel);
		wicketTester.assertVisible("test");
	}
	
	@Test
	public void testCreateKittyFail() throws Exception{
		TestModel<User> userModel = new TestModel<User>(createTestData());
		CreateKittyPanel createKittyPanel = new CreateKittyPanel("test", userModel);
		KittyService mockService = TestHelper.setAndGetServiceMock(createKittyPanel, KittyService.class);
		
		wicketTester.startComponentInPage(createKittyPanel);
		FormTester formTester = wicketTester.newFormTester("test:kittyForm");
		formTester.submit();
		verify(mockService, times(0)).createNewKitty(any(Kitty.class));
	}
	
	@Test
	public void testCreateKittySuccess() throws Exception{
		TestModel<User> userModel = new TestModel<User>(createTestData());
		CreateKittyPanel createKittyPanel = new CreateKittyPanel("test", userModel);
		KittyService mockService = TestHelper.setAndGetServiceMock(createKittyPanel, KittyService.class);
		
		wicketTester.startComponentInPage(createKittyPanel);
		FormTester formTester = wicketTester.newFormTester("test:kittyForm");
		formTester.setValue("name", "newKitty");
		formTester.submit();
		verify(mockService).createNewKitty(any(Kitty.class));
	}
	
	private static User createTestData() {
		User user = new User();
		user.setDisplayName("user1");
		return user;
	}
}
