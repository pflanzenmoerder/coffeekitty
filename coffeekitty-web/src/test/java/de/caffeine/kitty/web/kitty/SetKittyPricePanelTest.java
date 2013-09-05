package de.caffeine.kitty.web.kitty;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.service.KittyService;
import de.caffeine.kitty.web.tools.TestHelper;
import de.caffeine.kitty.web.tools.TestModel;


public class SetKittyPricePanelTest {
	
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
		TestModel<Kitty> kittyModel = new TestModel<Kitty>(createTestData());
		SetKittyPricePanel setKittyPricePanel = new SetKittyPricePanel("test", kittyModel);
		wicketTester.startComponentInPage(setKittyPricePanel);
		wicketTester.assertVisible("test");
	}
	
	@Test
	public void testSetPrice() throws Exception{
		TestModel<Kitty> kittyModel = new TestModel<Kitty>(createTestData());
		SetKittyPricePanel setKittyPricePanel = new SetKittyPricePanel("test", kittyModel);
		KittyService mockService = TestHelper.setAndGetServiceMock(setKittyPricePanel, KittyService.class);
		wicketTester.startComponentInPage(setKittyPricePanel);
		wicketTester.assertVisible("test");
		FormTester formTester = wicketTester.newFormTester("test:kittyPriceForm");
		formTester.setValue("pricePerMugInEuro", "3,5");
		formTester.submit();
		assertEquals(Float.valueOf(3.5f), kittyModel.getObject().getPricePerMugInEuro());
		verify(mockService).saveKitty(kittyModel.getObject());
	}
	
	private static Kitty createTestData() {
		Kitty ret = new Kitty();
		ret.setName("testName");
		return ret;
	}
}
