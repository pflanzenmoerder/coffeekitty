package de.caffeine.kitty.web.kitty;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.service.KittyService;
import de.caffeine.kitty.service.repository.kitty.KittySearchResult;
import de.caffeine.kitty.web.tools.TestHelper;
import de.caffeine.kitty.web.tools.TestModel;


public class SearchKittyFormPanelTest {
	
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
		TestModel<KittySearchResult> kittySearchResultModel = new TestModel<KittySearchResult>(null);
		SearchKittyFormPanel searchKittyFormPanel = new SearchKittyFormPanel("test", kittySearchResultModel);
		wicketTester.startComponentInPage(searchKittyFormPanel);
		wicketTester.assertVisible("test");
	}
	
	@Test
	public void testSearch() throws Exception{
		KittySearchResult result = createTestData();
		TestModel<KittySearchResult> kittySearchResultModel = new TestModel<KittySearchResult>(null);
		SearchKittyFormPanel searchKittyFormPanel = new SearchKittyFormPanel("test", kittySearchResultModel);
		wicketTester.startComponentInPage(searchKittyFormPanel);
		wicketTester.assertVisible("test");
		KittyService mockService = TestHelper.setAndGetServiceMock(searchKittyFormPanel, KittyService.class);
		when(mockService.findKittiesbyNameAndPageSizeAndPageId("na",10, 0)).thenReturn(result);
		
		wicketTester.startComponentInPage(searchKittyFormPanel);
		FormTester formTester = wicketTester.newFormTester("test:searchForm");
		formTester.setValue("search-name", "na");
		formTester.submit();
		assertEquals(result, kittySearchResultModel.getObject());
	}
	
	private static KittySearchResult createTestData() {
		KittySearchResult ret = new KittySearchResult();
		ret.total = 2l;
		ret.kitties = new ArrayList<Kitty>();
		for (int i = 0; i < ret.total; i++) {
			Kitty kitty = new Kitty();
			kitty.setName("name"+i);
			ret.kitties.add(kitty);
		}
		return ret;
	}
}
