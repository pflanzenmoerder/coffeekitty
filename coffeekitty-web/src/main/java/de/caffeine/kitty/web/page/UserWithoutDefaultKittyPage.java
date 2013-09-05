package de.caffeine.kitty.web.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.kitty.KittySearchResult;
import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.header.HeaderPanel;
import de.caffeine.kitty.web.kitty.KittySearchResultPanel;
import de.caffeine.kitty.web.kitty.SearchKittyFormPanel;
import de.caffeine.kitty.web.kitty.create.CreateKittyPanel;


@SuppressWarnings("serial")
@AuthorizeInstantiation(Roles.USER)
public class UserWithoutDefaultKittyPage extends BaseCafManPage {

	private static final String SEARCH_RESULT = "searchResult";
	private static final String SEARCH_KITTY = "searchKitty";
	private static final String CREATE_KITTY = "createKitty";

	public UserWithoutDefaultKittyPage() {
		IModel<User> userModel =  UserAuthenticatedWebSession.get().getUserModel();
		IModel<KittySearchResult> kittySearchResultModel = new Model<KittySearchResult>();
		add(new HeaderPanel("header"));
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
		}
	}
}
