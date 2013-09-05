package de.caffeine.kitty.web.page;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.kitty.ManageKittyMembersPanel;
import de.caffeine.kitty.web.kitty.SetKittyPricePanel;
import de.caffeine.kitty.web.user.notifications.DisplayNotificationsPanel;


@SuppressWarnings("serial")
@AuthorizeInstantiation(Roles.USER)
@Configurable
public class AdminKittyPage extends BaseCafManPage {
	private static final String SET_KITTY_PRICE = "setKittyPrice";
	private static final String MANAGE_KITTY_MEMBERS = "manageKittyMembers";
	private static final String DISPLAY_NOTIFICATIONS = "displayNotifications";

	@Autowired
	private transient AccountService accountService;
	
	@SuppressWarnings("serial")
	public AdminKittyPage(IModel<Kitty> kitty) {
		final IModel<User> userModel = UserAuthenticatedWebSession.get().getUserModel();
		final IModel<List<Account>> accounts = new LoadableDetachableModel<List<Account>>() {
				@Override
				protected List<Account> load() {
					return accountService.findAccountRequestsByUser(userModel.getObject());
				}
			};			
		add(new Label("kittyName", new PropertyModel<String>(kitty, "name")));
		add(new DisplayNotificationsPanel(DISPLAY_NOTIFICATIONS, accounts) {
	
				@Override
				protected void onConfigure() {
					super.onConfigure();
					setVisibilityAllowed(!accounts.getObject().isEmpty());
				}
				
			}
			.setOutputMarkupPlaceholderTag(true)
			.setOutputMarkupId(true));
		add(new SetKittyPricePanel(SET_KITTY_PRICE, kitty).setOutputMarkupId(true));
		add(new ManageKittyMembersPanel(MANAGE_KITTY_MEMBERS, new PropertyModel<List<Account>>(kitty,"accounts")).setOutputMarkupId(true));
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if (event.getPayload() instanceof AjaxRequestTarget) {
			AjaxRequestTarget target = ((AjaxRequestTarget)event.getPayload());
			target.add(get(DISPLAY_NOTIFICATIONS));
			target.add(get(MANAGE_KITTY_MEMBERS));
			target.add(get(SET_KITTY_PRICE));
		}
	}

}
