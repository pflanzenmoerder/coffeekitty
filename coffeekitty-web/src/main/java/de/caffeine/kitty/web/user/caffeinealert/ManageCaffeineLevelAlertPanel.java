package de.caffeine.kitty.web.user.caffeinealert;

import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.UserService;


/**
 * Panel to set up an email alert regarding the caffeine level for a user.
 */
@SuppressWarnings("serial")
@Configurable
public class ManageCaffeineLevelAlertPanel extends Panel {

	@Autowired
	private transient UserService userService;
	
	/**
	 * Constructor.
	 * @param id Component-ID.
	 * @param model Model to deliver the relevant user.
	 */
	public ManageCaffeineLevelAlertPanel(String id, final IModel<User> model) {
		super(id, model);
		@SuppressWarnings("unchecked")
		Form<Void> alertForm = (Form<Void>) new Form<Void>("alertForm") {
				@Override
				protected void onSubmit() {
					super.onSubmit();
					User user = model.getObject();
					user.setWarnLevel(Integer.valueOf((String)get("alert-amount").getDefaultModelObject()));
					userService.saveUser(user);
				}			
			}
			.add(new TextField<Integer>("alert-amount", Model.of(model.getObject().getWarnLevel())).setRequired(true));
		
		alertForm.add(new AjaxFallbackButton("alert-submit", alertForm) {});
		add(alertForm);
	}

}
