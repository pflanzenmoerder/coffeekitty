package de.caffeine.kitty.web.kitty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.service.AccountService;


@SuppressWarnings("serial")
@Configurable
public class ManageKittyMembersPanel extends Panel{

	@Autowired
	private transient AccountService accountService;
	
	public ManageKittyMembersPanel(String id, IModel<List<Account>> model) {
		super(id, model);
		final Map<String, Float> accountPayments = new HashMap<String, Float>();
		@SuppressWarnings("unchecked")
		Form<List<Account>> paymentsForm = (Form<List<Account>>)new Form<List<Account>>("paymentsForm", model) {
				@Override
				protected void onSubmit() {
					super.onSubmit();
					List<Account> accounts = new ArrayList<Account>(getModelObject());
					for (Account account : accounts) {
						if(accountPayments.get(account.getId()) != null) {
							account.setBalance(accountPayments.get(account.getId()));
							accountPayments.put(account.getId(), new Float(0));
						}
					}
					accountService.saveAccounts(accounts);
				}
			}
			.add(new ListView<Account>("membersList", model) {
				@Override
				protected void populateItem(ListItem<Account> item) {
					item.setModel(new CompoundPropertyModel<Account>(item.getModel()));
					item.add(new Label("user.fullName"));
					item.add(new Label("user.displayName"));
					item.add(new Label("balance"));
					accountPayments.put(item.getModelObject().getId(), new Float(0));
					item.add(new TextField<Float>("payment", new PropertyModel<Float>(accountPayments, "["+item.getModelObject().getId()+"]"), Float.class));
					item.add(new AttributeAppender("class", item.getModelObject().getBalance() >= 0 ? " text-success": " text-error"));
				}
			});
		paymentsForm.add(new AjaxFallbackButton("payments-submit", paymentsForm) {});
		add(paymentsForm);
	}
	
}
