package de.caffeine.kitty.web.kitty;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.service.KittyService;


@SuppressWarnings("serial")
@Configurable
public class SetKittyPricePanel extends Panel{

	@Autowired
	private transient KittyService kittyService;
	
	public SetKittyPricePanel(String id, IModel<Kitty> model) {
		super(id, model);
		add(new Form<Kitty>("kittyPriceForm", new CompoundPropertyModel<Kitty>(model)) {
				@Override
				protected void onSubmit() {
					super.onSubmit();
					kittyService.saveKitty(getModelObject());
				}
			}
			.add(new TextField<Float>("pricePerMugInEuro")));
	}

}
