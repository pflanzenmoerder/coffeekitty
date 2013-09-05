package de.caffeine.kitty.web.kitty.drink;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.web.tools.TestHelper;
import de.caffeine.kitty.web.tools.TestModel;


public class DrinkCoffeePanelTest {
	
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
		TestModel<Account> model = new TestModel<Account>(createTestData());
		DrinkCoffeePanel drinkCoffeePanel = new DrinkCoffeePanel("test", model);
		wicketTester.startComponentInPage(drinkCoffeePanel);
		wicketTester.assertVisible("test");
	}
	
	@Test
	public void testDrink() throws Exception{
		TestModel<Account> model = new TestModel<Account>(createTestData());
		DrinkCoffeePanel drinkCoffeePanel = new DrinkCoffeePanel("test", model);
		AccountService mockService = TestHelper.setAndGetServiceMock(drinkCoffeePanel, AccountService.class);
		wicketTester.startComponentInPage(drinkCoffeePanel);
		FormTester formTester = wicketTester.newFormTester("test:drinkCoffeeForm");
		formTester.submit();
		wicketTester.assertVisible("test");
		verify(mockService).drinkCoffee(any(Account.class));
	}
	
	private static Account createTestData() {
		Account account = new Account();
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(account);
		Kitty kitty = new Kitty();
		kitty.setPricePerMugInEuro(0.4f);
		kitty.setName("kittyName");
		kitty.setAccounts(accounts);
		return account;
	}
}
