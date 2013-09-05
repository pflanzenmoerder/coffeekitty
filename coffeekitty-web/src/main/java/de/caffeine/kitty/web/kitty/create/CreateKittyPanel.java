package de.caffeine.kitty.web.kitty.create;

import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataIntegrityViolationException;

import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.KittyService;
import de.caffeine.kitty.web.validation.JSR303FormValidator;


/**
 * Panel to start a new kitty.
 */
@SuppressWarnings("serial")
@Configurable
public class CreateKittyPanel extends Panel {

	@Autowired
	private transient KittyService kittyService;
	
	/**
	 * Constructor.
	 * @param id Component-ID.
	 * @param model Model to deliver the relevant user that starts the kitty.
	 */
	public CreateKittyPanel(String id, final IModel<User> model) {
		super(id, model);
		@SuppressWarnings("unchecked")
		Form<Kitty> kittyForm = (Form<Kitty>)new Form<Kitty>("kittyForm", new CompoundPropertyModel<Kitty>(new Kitty())) {
					@Override
					protected void onSubmit() {
						Kitty kitty = getModelObject();
						kitty.setUser(model.getObject());
						try {
							kittyService.createNewKitty(kitty);
							setDefaultModelObject(new Kitty());
						}
						catch (DataIntegrityViolationException violation) {
							//Spec implies to silently ignore this
						}
						
					}				
				}
				.add(new TextField<String>("name").setMarkupId("create-name"))
				.add(new JSR303FormValidator());
		kittyForm.add(new AjaxFallbackButton("create-submit", kittyForm) {});
		add(kittyForm);
	}

}
