package de.caffeine.kitty.web.page;

import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.service.UserService;
import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.header.HeaderPanel;
import de.caffeine.kitty.web.kitty.SelectKittyPanel;
import de.caffeine.kitty.web.kitty.drink.DrinkCoffeePanel;
import de.caffeine.kitty.web.user.notifications.DisplayNotificationsPanel;


@SuppressWarnings("serial")
@AuthorizeInstantiation(Roles.USER)
@Configurable
public class DrinkCoffeePage extends BaseCafManPage {

	private static final String DISPLAY_NOTIFICATIONS = "displayNotifications";

	@Autowired
	private transient AccountService accountService;
	
	@Autowired
	private transient UserService userService;
	
	@SuppressWarnings("serial")
	public DrinkCoffeePage() {
		final IModel<User> userModel = UserAuthenticatedWebSession.get().getUserModel();
		final IModel<List<Account>> accounts = new LoadableDetachableModel<List<Account>>() {
				@Override
				protected List<Account> load() {
					return accountService.findAccountRequestsByUser(userModel.getObject());
				}
			};			
			
		add(new HeaderPanel("header"));
		add(new DisplayNotificationsPanel(DISPLAY_NOTIFICATIONS, accounts) {

				@Override
				protected void onConfigure() {
					super.onConfigure();
					setVisibilityAllowed(!accounts.getObject().isEmpty());
				}
			}
			.setOutputMarkupId(true)
			.setOutputMarkupPlaceholderTag(true));
		IModel<Account> accounIModel = new PropertyModel<Account>(userModel, "defaultAccount");
		add(new SelectKittyPanel("chooseDefaultKitty", 
				accounIModel, 
				new SortedAccountListModel(new PropertyModel<Set<Account>>(userModel, "accounts"))) {
					@Override
					protected void onKittySelected(Account account) {
						User user = userModel.getObject();
						user.setDefaultAccount(account);
						userService.saveUser(user);
					}
				});
		add(new DrinkCoffeePanel("drinkCoffee", accounIModel));
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if (event.getPayload() instanceof AjaxRequestTarget) {
			AjaxRequestTarget target = ((AjaxRequestTarget)event.getPayload());
			target.add(get(DISPLAY_NOTIFICATIONS));
		}
	}

}
