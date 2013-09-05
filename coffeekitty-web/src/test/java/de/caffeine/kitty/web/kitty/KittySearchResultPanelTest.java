package de.caffeine.kitty.web.kitty;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.service.repository.kitty.KittySearchResult;
import de.caffeine.kitty.web.tools.TestHelper;
import de.caffeine.kitty.web.tools.TestModel;


public class KittySearchResultPanelTest {
	
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
		KittySearchResult kittySearchResult = new KittySearchResult();
		kittySearchResult.total=(long) userModel.getObject().getKitties().size();
		kittySearchResult.kitties=new ArrayList<Kitty>(userModel.getObject().getKitties());
		Kitty requestableKitty = new Kitty();
		requestableKitty.setName("requestableKitty");
		kittySearchResult.kitties.add(requestableKitty);
		
		TestModel<KittySearchResult> resultModel = new TestModel<KittySearchResult>(kittySearchResult);
		KittySearchResultPanel kittySearchResultPanel = new KittySearchResultPanel("test", userModel, resultModel);
		wicketTester.startComponentInPage(kittySearchResultPanel);
		wicketTester.assertVisible("test");
	}
	
	@Test
	public void testButtonsOnylVisibleForKittiesWithoutMemebership() {
		TestModel<User> userModel = new TestModel<User>(createTestData());
		KittySearchResult kittySearchResult = new KittySearchResult();
		kittySearchResult.total=(long) userModel.getObject().getKitties().size();
		kittySearchResult.kitties=new ArrayList<Kitty>(userModel.getObject().getKitties());
		Kitty requestableKitty = new Kitty();
		requestableKitty.setName("requestableKitty");
		kittySearchResult.kitties.add(requestableKitty);
		
		TestModel<KittySearchResult> resultModel = new TestModel<KittySearchResult>(kittySearchResult);
		KittySearchResultPanel kittySearchResultPanel = new KittySearchResultPanel("test", userModel, resultModel);
		wicketTester.startComponentInPage(kittySearchResultPanel);
		wicketTester.assertVisible("test");
		wicketTester.assertInvisible("test:searchResults:0:requestButton");
		wicketTester.assertInvisible("test:searchResults:1:requestButton");
		wicketTester.assertVisible("test:searchResults:2:requestButton");
	}
	
	@Test
	public void testRequestMemebership() throws Exception{
		TestModel<User> userModel = new TestModel<User>(createTestData());
		KittySearchResult kittySearchResult = new KittySearchResult();
		kittySearchResult.total=(long) userModel.getObject().getKitties().size();
		kittySearchResult.kitties=new ArrayList<Kitty>(userModel.getObject().getKitties());
		Kitty requestableKitty = new Kitty();
		requestableKitty.setName("requestableKitty");
		kittySearchResult.kitties.add(requestableKitty);
		
		TestModel<KittySearchResult> resultModel = new TestModel<KittySearchResult>(kittySearchResult);
		KittySearchResultPanel kittySearchResultPanel = new KittySearchResultPanel("test", userModel, resultModel);
		AccountService mockService = TestHelper.setAndGetServiceMock(kittySearchResultPanel, AccountService.class);
		
		wicketTester.startComponentInPage(kittySearchResultPanel);
		wicketTester.clickLink("test:searchResults:2:requestButton");
		
		verify(mockService).requestAccountForKitty(userModel.getObject(), requestableKitty);
	}
	
	
	private static User createTestData() {
		User user = new User();
		user.setDisplayName("user1");
		
		Kitty kitty = new Kitty();
		kitty.setName("kitty1");
		user.addKitty(kitty);
		Account account = new Account();
		account.setUser(user);
		account.setKitty(kitty);
		user.addAccount(account);
		
		kitty = new Kitty();
		kitty.setName("kitty2");
		user.addKitty(kitty);
		account = new Account();
		account.setUser(user);
		account.setKitty(kitty);
		user.addAccount(account);
		
		return user;
	}
}
