package de.caffeine.kitty.web.navigation;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.page.DisplayUserCaffeineStatisticsPage;
import de.caffeine.kitty.web.page.DrinkCoffeePage;
import de.caffeine.kitty.web.page.HiscoresPage;
import de.caffeine.kitty.web.page.ManageKittiesPage;
import de.caffeine.kitty.web.page.ProfilePage;

import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;

@SuppressWarnings("serial")
public class NavbarPanel extends Panel {
	public NavbarPanel(String id, IModel<User> userModel) {
		super(id, userModel);

		//class="navbar navbar-inverse navbar-fixed-top"
		final Navbar navbar = new Navbar("navbar");
		navbar.setOutputMarkupId(true);
		navbar.setRenderBodyOnly(false);

		navbar.setPosition(Navbar.Position.TOP);
		navbar.invert(true);
		navbar.brandName(Model.of("CafMan by Senacor"));

		// Home
		final AjaxFallbackLink<String> home = new AjaxFallbackLink<String>("button") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(DrinkCoffeePage.class);
			}
		};

		//Caffeine Statistics
		UserNavbarButton caffeineStatistics
			= new UserNavbarButton(DisplayUserCaffeineStatisticsPage.class,
			new StringResourceModel("caffeineStatisticsButtonLabel", this, null, "Caffeine Statistics"));

		//Highscores
		NavbarButton<Page> highscores
			= new NavbarButton<Page>(HiscoresPage.class,
			new StringResourceModel("caffeineHighscoresButtonLabel", this, null, "Highscores"));

		// Coffee Kitty
		UserNavbarButton coffeeKitties
			= new UserNavbarButton(ManageKittiesPage.class,
			new StringResourceModel("manageKittiesButtonLabel", this, null, "Coffee Kitty"));

		// add the left buttons
		navbar.addButton(Navbar.ButtonPosition.LEFT, home, caffeineStatistics, highscores, coffeeKitties);

		// User Profile
		UserNavbarButton userProfile
			= new UserNavbarButton(ProfilePage.class, new LoadableDetachableModel<String>() {
				@Override
				protected String load() {
					User user = (User)getDefaultModelObject();
					return user.getDisplayName()+" - "+user.getEmail();
				}
			});

		// Logout
		AjaxFallbackLink<String> logout = new LogoutLink("button");

		navbar.addButton(Navbar.ButtonPosition.RIGHT, userProfile, logout);

		add(navbar);
	}
	
	@AuthorizeAction(action=Action.RENDER,roles = {Roles.ADMIN, Roles.USER})
	private static final class LogoutLink extends AjaxFallbackLink<String> {
		private LogoutLink(String id) {
			super(id);
			setEscapeModelStrings(false);
		}

		@Override
		public void onClick(AjaxRequestTarget target) {
			UserAuthenticatedWebSession.get().invalidateNow();
			setResponsePage(Application.get().getHomePage());
		}

		@Override
		public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
			replaceComponentTagBody(markupStream, openTag, "<i class=\"icon-off icon-white\"></i> Sign out");
		}
	}
	
	@SuppressWarnings("rawtypes")
	@AuthorizeAction(action=Action.RENDER,roles = {Roles.USER})
	private class UserNavbarButton extends NavbarButton {
		@SuppressWarnings("unchecked")
		public UserNavbarButton(Class pageClass, IModel<?> label) {
			super(pageClass, label);
		}
	}
}
