package de.caffeine.kitty.web.page;

import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.service.repository.kitty.KittySearchResult;
import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.kitty.KittySearchResultPanel;
import de.caffeine.kitty.web.kitty.SearchKittyFormPanel;
import de.caffeine.kitty.web.kitty.create.CreateKittyPanel;
import de.caffeine.kitty.web.user.account.ListAccountsPanel;
import de.caffeine.kitty.web.user.notifications.DisplayNotificationsPanel;


@SuppressWarnings("serial")
@AuthorizeInstantiation(Roles.USER)
@Configurable
public class ManageKittiesPage extends BaseCafManPage {
	
	private static final String SEARCH_RESULT = "searchResult";
	private static final String SEARCH_KITTY = "searchKitty";
	private static final String CREATE_KITTY = "createKitty";
	private static final String DISPLAY_NOTIFICATIONS = "displayNotifications";
	private static final String LIST_ACCOUNTS = "listAccounts";

	@Autowired
	private transient AccountService accountService;
	
	@SuppressWarnings("serial")
	public ManageKittiesPage() {
		final IModel<User> userModel = UserAuthenticatedWebSession.get().getUserModel();
		final IModel<List<Account>> accounts = new LoadableDetachableModel<List<Account>>() {
				@Override
				protected List<Account> load() {
					return accountService.findAccountRequestsByUser(userModel.getObject());
				}
			};			
		add(new ListAccountsPanel(LIST_ACCOUNTS, new SortedAccountListModel(new PropertyModel<Set<Account>>(userModel, "accounts"))).setOutputMarkupId(true));
		add(new DisplayNotificationsPanel(DISPLAY_NOTIFICATIONS, accounts) {
				@Override
				protected void onConfigure() {
					super.onConfigure();
					setVisibilityAllowed(!accounts.getObject().isEmpty());
				}
			}
			.setOutputMarkupPlaceholderTag(true)
			.setOutputMarkupId(true));
		IModel<KittySearchResult> kittySearchResultModel = new Model<KittySearchResult>();
		add(new CreateKittyPanel(CREATE_KITTY, userModel).setOutputMarkupId(true));
		add(new SearchKittyFormPanel(SEARCH_KITTY, kittySearchResultModel).setOutputMarkupId(true));
		add(new KittySearchResultPanel(SEARCH_RESULT, userModel, kittySearchResultModel).setOutputMarkupId(true));
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if (event.getPayload() instanceof AjaxRequestTarget) {
			AjaxRequestTarget target = ((AjaxRequestTarget)event.getPayload());
			target.add(get(SEARCH_KITTY));
			target.add(get(SEARCH_RESULT));
			target.add(get(CREATE_KITTY));
			target.add(get(DISPLAY_NOTIFICATIONS));
			target.add(get(LIST_ACCOUNTS));
		}
	}
}
