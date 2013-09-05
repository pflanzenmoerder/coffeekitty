package de.caffeine.kitty.web.highscore;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.caffeine.kitty.service.repository.consumtion.HiscoreDTO;
import de.caffeine.kitty.web.common.CurrentUserModel;
import de.caffeine.kitty.web.page.DisplayUserCaffeineStatisticsPage;


/**
 * Panel to display highscores for a certain time frame,
 * for example the last 7 days. The objects to be displayed
 * are determined by the given model.
 */
@SuppressWarnings("serial")
public class DisplayCaffeineHighscoresPanel extends Panel {

	/**
	 * Constructor.
	 * @param id Component-ID.
	 * @param model Model to deliver relevant highscore objects.
	 */
	public DisplayCaffeineHighscoresPanel(String id, IModel<List<HiscoreDTO>> consumptionModel) {
		super(id, consumptionModel);
		add(new ListView<HiscoreDTO>("highscoreView", consumptionModel) {

			@SuppressWarnings("rawtypes")
			@Override
			protected void populateItem(ListItem<HiscoreDTO> item) {
				final HiscoreDTO hiscoreDTO = item.getModelObject();
				String level = hiscoreDTO.caffeineLevel != null ? hiscoreDTO.caffeineLevel.toString() : "";
				String name = hiscoreDTO.publish ? hiscoreDTO.displayName : getString("anonymous");
				if (hiscoreDTO.displayName == null) {
					name = "";
				}
				item.add(new AjaxFallbackLink("caffeineLink") {
							@Override
							public void onClick(AjaxRequestTarget target) {
								//Redirect to Hiscore page of selected user
								setResponsePage(new DisplayUserCaffeineStatisticsPage(new CurrentUserModel(hiscoreDTO.userId)));
							}
						}
						.add(new Label("name", name))
						.setEnabled(hiscoreDTO.publish))
					.add(new Label("level", level))
					.add(new Label("rank", Integer.toString(item.getIndex()+1)));
				
			}
			
		});
	}

}
