package de.caffeine.kitty.web.kitty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.AccountStatusEnum;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.service.repository.kitty.KittySearchResult;

/**
 * Panel for displaying the results of a kitty search.
 * @see KittySearchResult
 */
@SuppressWarnings("serial")
@Configurable
public class KittySearchResultPanel extends Panel{
	
	private static final String KITTY_NAME = "name";
	public static final String SEARCH_RESULTS = "searchResults";
	public static final String PAGINATION = "pagination";
	public static final String SEARCH_RESULT = "searchResult";
	
	@Autowired
	private transient AccountService accountService;
	
	private IModel<KittySearchResult> kittySearchresultsModel;
	private IModel<Map<Kitty, AccountStatusEnum>> userKitties;
	public KittySearchResultPanel(String id, final IModel<User> userModel, IModel<KittySearchResult> kittySearchresultsModel) {
		super(id, userModel);
		this.kittySearchresultsModel = kittySearchresultsModel;
		this.userKitties = new UserKittiesModel(userModel);
		add(new ListView<Kitty>(SEARCH_RESULTS, new PropertyModel<List<Kitty>>(kittySearchresultsModel, "kitties")) {
				@Override
				protected void populateItem(ListItem<Kitty> item) {
					Kitty kitty = item.getModelObject();
					item.add(new Label(KITTY_NAME, new PropertyModel<String>(item.getModel(), KITTY_NAME)));
					item.add(new AjaxLink<Kitty>("requestButton", item.getModel()) {
						@Override
						public void onClick(AjaxRequestTarget target) {
							accountService.requestAccountForKitty((User)KittySearchResultPanel.this.getDefaultModelObject(), getModelObject());
							setVisibilityAllowed(false);
							target.add(this);
						}
					}.setVisibilityAllowed(!userKitties.getObject().keySet().contains(kitty)).setOutputMarkupId(true));
					if(AccountStatusEnum.APPROVED.equals(userKitties.getObject().get(kitty))) {						
						item.add(new AttributeModifier("class", "success"));
						item.add(new Label("accountExistsLabel","Already a Member"));
					}
					else if(AccountStatusEnum.REQUESTED.equals(userKitties.getObject().get(kitty))) {						
						item.add(new AttributeModifier("class", "warning"));
						item.add(new Label("accountExistsLabel","Membership Request Pending"));
					}
					else {
						item.add(new Label("accountExistsLabel",""));
					}
				}
			});
		add(new WebMarkupContainer(PAGINATION));
		setOutputMarkupPlaceholderTag(true);
	}
	

	@Override
	protected void onConfigure() {
		super.onConfigure();
		KittySearchResult kittySearchResult = kittySearchresultsModel.getObject();
		setVisibilityAllowed(kittySearchResult != null && kittySearchResult.kitties.size()>0);
	}


	@Override
	protected void onDetach() {
		super.onDetach();
		kittySearchresultsModel.detach();
		userKitties.detach();
	}


	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if (event.getPayload() instanceof AjaxRequestTarget) {
			((AjaxRequestTarget)event.getPayload()).add(this);
		}
	}
	
	private static class UserKittiesModel extends LoadableDetachableModel<Map<Kitty, AccountStatusEnum>> {
		private IModel<User> userModel;
		
		public UserKittiesModel(IModel<User> userModel) {
			super();
			this.userModel = userModel;
		}

		@Override
		protected Map<Kitty, AccountStatusEnum> load() {
			HashMap<Kitty, AccountStatusEnum> kitties = new HashMap<Kitty, AccountStatusEnum>();
			for (Account account : userModel.getObject().getAccounts()) {
				kitties.put(account.getKitty(), account.getAccountStatusEnum());
			}
			return kitties;
		}

		@Override
		protected void onDetach() {
			super.onDetach();
			userModel.detach();
		}
	}
	
}
