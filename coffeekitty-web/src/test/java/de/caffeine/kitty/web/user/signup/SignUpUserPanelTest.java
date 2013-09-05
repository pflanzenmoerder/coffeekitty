package de.caffeine.kitty.web.user.signup;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SignUpUserPanelTest {
	
	private WicketTester wicketTester;
	
	@Before
	public void setUp() {
		wicketTester = new WicketTester();
	}
	
	@After
	public void tearDown() {
		wicketTester.destroy();
	}
	
	@Test
	public void testRender() { 
		SignUpUserPanel signUpUserPanel = new SignUpUserPanel("test");
		wicketTester.startComponentInPage(signUpUserPanel);
		wicketTester.assertInvisible("test:signup-feedback");
		wicketTester.assertVisible("test:signup-form");
	}
	
	
}
