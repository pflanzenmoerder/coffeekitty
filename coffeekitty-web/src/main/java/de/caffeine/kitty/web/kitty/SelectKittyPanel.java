package de.caffeine.kitty.web.kitty;

import java.util.List;

import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;


/**
 * Panel to select a kitty of a user.
 */
@SuppressWarnings("serial")
@Configurable
public abstract class SelectKittyPanel extends Panel {

	/**
	 * Constructor.
	 * @param id Component-ID.
	 * @param model Model to deliver the relevant kitties, for example all kitties of a user.
	 */
	@SuppressWarnings("unchecked")
	public SelectKittyPanel(String id, IModel<Account> defaultAccount, IModel<List<Account>> model) {
		super(id, model);
		Form<Void> form = new Form<Void>("selectKittyForm") {
			@Override
			protected void onSubmit() {
				super.onSubmit();
				final IModel<Account> defaultKittyModel = (IModel<Account>) get("kitty-select").getDefaultModel();
				final Account account = defaultKittyModel.getObject();
				onKittySelected(account);
			}
		};

		DropDownChoice<Account> dropDownChoice = new DropDownChoice<Account>("kitty-select", defaultAccount, model);
		dropDownChoice.setChoiceRenderer(new ChoiceRenderer<Account>("kitty.name"));
		dropDownChoice.setMarkupId("kitty-select");
		form.add(new AjaxFallbackButton("kitty-submit", form){});
		form.add(dropDownChoice);
		add(form);
	}

	/**
	 * Method that is called when an account has been selected.
	 * @param account The selected account.
	 */
	protected abstract void onKittySelected(Account account);

}
