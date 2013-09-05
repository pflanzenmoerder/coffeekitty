package de.caffeine.kitty.web;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authentication.strategy.DefaultAuthenticationStrategy;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.web.common.OpenEntityManagerInRequestCycleListener;
import de.caffeine.kitty.web.page.AdminPage;
import de.caffeine.kitty.web.page.DisplayUserCaffeineStatisticsPage;
import de.caffeine.kitty.web.page.DrinkCoffeePage;
import de.caffeine.kitty.web.page.HiscoresPage;
import de.caffeine.kitty.web.page.ManageKittiesPage;
import de.caffeine.kitty.web.page.ProfilePage;
import de.caffeine.kitty.web.page.SignInUserPage;
import de.caffeine.kitty.web.page.UserWithoutDefaultKittyPage;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.BootstrapSettings;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 */
@Configurable
public class WicketApplication extends AuthenticatedWebApplication {
	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		if (UserAuthenticatedWebSession.get().isAdmin()) {
			return AdminPage.class;
		}
		else if (UserAuthenticatedWebSession.get().isSignedIn() && UserAuthenticatedWebSession.get().getUserModel().getObject().getDefaultAccount() != null) {
				return DrinkCoffeePage.class;	
		}
		else {
			return UserWithoutDefaultKittyPage.class;
		}
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();
		getSecuritySettings().setAuthenticationStrategy(new DefaultAuthenticationStrategy("WUUUT"));
		getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
		getRequestCycleListeners().add(new OpenEntityManagerInRequestCycleListener());
		if (RuntimeConfigurationType.DEVELOPMENT.equals(getConfigurationType())) {
			mountPage("/signin", getSignInPageClass());
			mountPage("/profile", ProfilePage.class);
			mountPage("/drink", DrinkCoffeePage.class);
			mountPage("/caffeine", DisplayUserCaffeineStatisticsPage.class);
			mountPage("/highscores", HiscoresPage.class);
			mountPage("/kitties", ManageKittiesPage.class);
		}
		Bootstrap.install(this, new BootstrapSettings().useResponsiveCss(false));
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return UserAuthenticatedWebSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return SignInUserPage.class;
	}

}
