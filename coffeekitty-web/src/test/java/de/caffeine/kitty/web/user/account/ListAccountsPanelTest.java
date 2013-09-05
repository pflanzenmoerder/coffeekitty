package de.caffeine.kitty.web.user.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.web.tools.TestModel;
import de.caffeine.kitty.web.user.account.ListAccountsPanel;


public class ListAccountsPanelTest {
	
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
		ListAccountsPanel listAccountsPanel = new ListAccountsPanel("test", accountsModel);
		wicketTester.startComponentInPage(listAccountsPanel);
		wicketTester.assertVisible("test");
		wicketTester.assertVisible("test:accountRow:0:adminLink");
		wicketTester.assertInvisible("test:accountRow:1:adminLink");
		wicketTester.assertInvisible("test:accountRow:2:adminLink");
		wicketTester.assertInvisible("test:accountRow:3:adminLink");
	}
	
	private static List<Account> createTestData() {
		List<Account> ret = new ArrayList<Account>();
		for (int i=0; i<4; i++){
			Account account = new Account();
			Kitty kitty = new Kitty();
			kitty.setName("kitty1");
			account.setKitty(kitty);
			account.setAdmin(i == 0);
			ret.add(account);	
		}
		return ret;
	}
}
