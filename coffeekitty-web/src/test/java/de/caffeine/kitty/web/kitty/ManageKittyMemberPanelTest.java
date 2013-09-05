package de.caffeine.kitty.web.kitty;

import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.web.tools.TestHelper;
import de.caffeine.kitty.web.tools.TestModel;


public class ManageKittyMemberPanelTest {
	
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
		List<Account> accountList = Collections.unmodifiableList(createTestData());
		ManageKittyMembersPanel manageKittyMembersPanel = new ManageKittyMembersPanel("test", new TestModel<List<Account>>(accountList));
		wicketTester.startComponentInPage(manageKittyMembersPanel);
		wicketTester.assertVisible("test");
		wicketTester.assertListView("test:paymentsForm:membersList", accountList);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateBalance() throws Exception {
		List<Account> accountList = Collections.unmodifiableList(createTestData());
		ManageKittyMembersPanel manageKittyMembersPanel = new ManageKittyMembersPanel("test", new TestModel<List<Account>>(accountList));
		AccountService mockService = TestHelper.setAndGetServiceMock(manageKittyMembersPanel, AccountService.class);
		wicketTester.startComponentInPage(manageKittyMembersPanel);
		FormTester formTester = wicketTester.newFormTester("test:paymentsForm");
		formTester.setValue("membersList:0:payment", "3");
		formTester.submit();
		verify(mockService).saveAccounts(anyCollection());
	}

	
	private static List<Account> createTestData() {
		List<Account> ret = new ArrayList<Account>();
		User user = new User();
		user.setDisplayName("user1");
		Account account = new Account();
		account.setBalance(-2.0f);
		account.setUser(user);
		ret.add(account);
		
		user = new User();
		user.setDisplayName("user2");
		account = new Account();
		account.setBalance(1.0f);
		account.setUser(user);
		ret.add(account);
		
		return ret;
	}
}
