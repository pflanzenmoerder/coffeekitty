package de.caffeine.kitty.web.user.notifications;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.web.tools.TestHelper;
import de.caffeine.kitty.web.tools.TestModel;


public class DisplayNotificationsPanelTest {
	
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
		TestModel<List<Account>> accountsModel = new TestModel<List<Account>>(createTestData());
		DisplayNotificationsPanel displayNotificationsPanel = new DisplayNotificationsPanel("test", accountsModel);
		wicketTester.startComponentInPage(displayNotificationsPanel);
		wicketTester.assertVisible("test");
	}
	
	@Test
	public void testAccept() throws Exception{
		TestModel<List<Account>> accountsModel = new TestModel<List<Account>>(createTestData());
		DisplayNotificationsPanel displayNotificationsPanel = new DisplayNotificationsPanel("test", accountsModel);
		AccountService mockService = TestHelper.setAndGetServiceMock(displayNotificationsPanel, AccountService.class);

		wicketTester.startComponentInPage(displayNotificationsPanel);
		wicketTester.assertVisible("test");
		wicketTester.clickLink("test:notificationsRepeater:1:acceptButton");
		verify(mockService).acceptMembership(accountsModel.getObject().get(1));
	}

	@Test
	public void testReject() throws Exception{
		TestModel<List<Account>> accountsModel = new TestModel<List<Account>>(createTestData());
		DisplayNotificationsPanel displayNotificationsPanel = new DisplayNotificationsPanel("test", accountsModel);
		AccountService mockService = TestHelper.setAndGetServiceMock(displayNotificationsPanel, AccountService.class);

		wicketTester.startComponentInPage(displayNotificationsPanel);
		wicketTester.assertVisible("test");
		wicketTester.clickLink("test:notificationsRepeater:1:declineButton");
		verify(mockService).rejectMembership(accountsModel.getObject().get(1));
	}
	
	private static List<Account> createTestData() {
		List<Account> ret = new ArrayList<Account>();
		for (int i=0;i<4;i++) {
			User user = new User();
			user.setDisplayName("displayName"+i);
			user.setFullName("fullName"+i);
			Kitty kitty = new Kitty();
			kitty.setName("kitty"+i);
			Account account = new Account();
			account.setUser(user);
			account.setKitty(kitty);
			ret.add(account);
		}
		return ret; 
	}
}
