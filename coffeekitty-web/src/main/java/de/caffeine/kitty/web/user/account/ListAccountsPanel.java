package de.caffeine.kitty.web.user.account;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.web.page.AdminKittyPage;


@SuppressWarnings("serial")
public class ListAccountsPanel extends Panel{
	public ListAccountsPanel(String id, IModel<List<Account>> accountsModel) {
		super(id, accountsModel);
		add(new ListView<Account>("accountRow", accountsModel){
			@Override
			protected void populateItem(final ListItem<Account> item) {
				item.add(new Label("name", item.getModelObject().getKitty().getName()));
				Float balance = item.getModelObject().getBalance();
				item.add(new Label("balance", "€ "+balance).add(new AttributeAppender("class", balance >= 0 ? " text-success" : " text-error")));
				item.add(new AjaxFallbackLink<Account>("adminLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setResponsePage(new AdminKittyPage(new PropertyModel<Kitty>(item.getModel(),"kitty")));
					}
				}.setVisibilityAllowed(item.getModelObject().getAdmin()));
			}
		});
	}

}
