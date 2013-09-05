package de.caffeine.kitty.web.page;

import org.apache.wicket.markup.html.WebPage;

import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.navigation.NavbarPanel;

/**
 * Abstract base class for pages within the CafMan application.
 */
@SuppressWarnings("serial")
public abstract class BaseCafManPage extends WebPage {

	protected BaseCafManPage() {
		add(new NavbarPanel("navbarPanel", UserAuthenticatedWebSession.get().getUserModel()).setRenderBodyOnly(true));
	}

}
