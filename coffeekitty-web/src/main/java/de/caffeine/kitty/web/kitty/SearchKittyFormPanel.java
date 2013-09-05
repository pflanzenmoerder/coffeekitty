package de.caffeine.kitty.web.kitty;

import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.service.KittyService;
import de.caffeine.kitty.service.repository.kitty.KittySearchResult;


@SuppressWarnings("serial")
@Configurable
public class SearchKittyFormPanel extends Panel{
	private static final String SEARCH_NAME = "search-name";
	@Autowired
	private transient KittyService kittyService;
	
	public SearchKittyFormPanel(String id, final IModel<KittySearchResult> kittySearchresultsModel) {
		super(id, kittySearchresultsModel);
		
		@SuppressWarnings("unchecked")
		Form<Void> searchForm = (Form<Void>)new Form<Void>("searchForm") {
				@Override
				protected void onSubmit() {
					super.onSubmit();
					KittySearchResult searchResult = kittyService.findKittiesbyNameAndPageSizeAndPageId(get(SEARCH_NAME).getDefaultModelObjectAsString(),10,0);
					SearchKittyFormPanel.this.setDefaultModelObject(searchResult);
				}
			}
			.add(new TextField<String>(SEARCH_NAME, Model.of(""))
					.setRequired(true)
					.setMarkupId(SEARCH_NAME));
		
		searchForm.add(new AjaxFallbackButton("search-submit", searchForm) {});
		add(searchForm);
	}
}
