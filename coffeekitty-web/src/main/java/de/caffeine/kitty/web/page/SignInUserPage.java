package de.caffeine.kitty.web.page;

import de.caffeine.kitty.web.header.HeaderPanel;
import de.caffeine.kitty.web.user.signin.SignInUserPanel;
import de.caffeine.kitty.web.user.signup.SignUpUserPanel;

@SuppressWarnings("serial")
public class SignInUserPage extends BaseCafManPage {

	public SignInUserPage() {

		add(new HeaderPanel("header"));
		add(new SignUpUserPanel("signup-panel"));
		add(new SignInUserPanel("signin-panel"));

	}
}
