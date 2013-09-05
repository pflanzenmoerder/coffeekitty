package de.caffeine.kitty.web.kitty.drink;

import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.service.AccountService;

/**
 * Panel providing a button to drink coffee.
 */
@SuppressWarnings("serial")
@Configurable
public class DrinkCoffeePanel extends Panel{

	@Autowired
	private transient AccountService accountService;
	
	public DrinkCoffeePanel(String id, IModel<Account> model) {
		super(id, model);
		@SuppressWarnings("unchecked")
		Form<Account> drinkCoffeeForm = (Form<Account>)new Form<Account>("drinkCoffeeForm", model) {
			@Override
			protected void onSubmit() {
				accountService.drinkCoffee(getModelObject());
			}
		}
		.add(new Label("infoMessage", new StringResourceModel("infoMessageText", this, new CompoundPropertyModel<Account>(model))));
		drinkCoffeeForm.add(new AjaxFallbackButton("consume-coffee", drinkCoffeeForm) {});
		add(drinkCoffeeForm);
	}

}
