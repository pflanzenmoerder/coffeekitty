package de.caffeine.kitty.web.kitty;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.web.tools.TestModel;


public class SelectKittyPanelTest {
	
	private static WicketTester wicketTester;
	
	@BeforeClass
	public static void setUp() {
		wicketTester = new WicketTester();
	}
	
	@AfterClass
	public static void tearDown() {
		wicketTester = new WicketTester();
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testRender() {
		TestModel<List<Account>> kittySearchResultModel = new TestModel<List<Account>>(createTestData());
		TestModel<Account> defAccountModel = new TestModel<Account>(kittySearchResultModel.getObject().get(0));
		SelectKittyPanel selectKittyPanel = new SelectKittyPanel("test", defAccountModel, kittySearchResultModel) {
			
			@Override
			protected void onKittySelected(Account account) {
			}
		};
		wicketTester.startComponentInPage(selectKittyPanel);
		wicketTester.assertVisible("test");
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testSelectKitty() {
		TestModel<List<Account>> kittySearchResultModel = new TestModel<List<Account>>(createTestData());
		TestModel<Account> defAccountModel = new TestModel<Account>(kittySearchResultModel.getObject().get(0));
		SelectKittyPanel selectKittyPanel = new SelectKittyPanel("test", defAccountModel, kittySearchResultModel) {
			
			@Override
			protected void onKittySelected(Account account) {
			}
		};
		wicketTester.startComponentInPage(selectKittyPanel);
		wicketTester.assertVisible("test");
		FormTester formTester = wicketTester.newFormTester("test:selectKittyForm");
		formTester.select("kitty-select", 1);
		formTester.submit();
		assertEquals(kittySearchResultModel.getObject().get(1), defAccountModel.getObject());
	}
	
	private static List<Account> createTestData() {
		List<Account> ret = new ArrayList<Account>();
		for (int i = 0; i < 5; i++) {
			Account account = new Account();
			Kitty kitty = new Kitty();
			kitty.setName("kitty"+i);
			account.setKitty(kitty);
			account.setAdmin(i == 0);
			ret.add(account);
		}
		return ret;
	}
}
