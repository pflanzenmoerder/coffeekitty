package de.caffeine.kitty.web.caffeine;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import de.caffeine.kitty.service.CaffeineDTO;
import de.caffeine.kitty.web.common.JFreeChartImage;

/**
 * Panel to display the caffeine levels of a user for a certain time frame.
 */
@SuppressWarnings("serial")
public class DisplayUserCaffeineLevelsPanel extends Panel {

	private static final int CHART_HEIGHT = 300;
	private static final int CHART_WIDTH = 600;

	/**
	 * Constructor.
	 * 
	 * @param id Wicket id of the componente
	 * @param chartDataModel model containing data points for generating the chart
	 * @param csvDataModel model containing the data points for generating the downloadable csv
	 * @param mode scale of the diagram (HOUR/DAY)
	 * @param rangeTextModel text for the diagram
	 */
	public DisplayUserCaffeineLevelsPanel(String id, IModel<List<CaffeineDTO>> chartDataModel, IModel<List<CaffeineDTO>> csvDataModel, CaffeineLevelDisplayMode mode, IModel<String> rangeTextModel) {
		super(id);
		
		add(new Label("heading", new StringResourceModel("heading", (IModel<?>)null, rangeTextModel)));
		add(new Label("description", new StringResourceModel("description", (IModel<?>)null, rangeTextModel)));
		add(new JFreeChartImage("chart", new TimeSeriesChartModel(chartDataModel, mode.getDateTickUnit()), CHART_WIDTH, CHART_HEIGHT));
		
		final DownloadLink csvDownloadLink = new DownloadLink("csvdownload", new GeneratingCSVFileModel(csvDataModel, mode));
		csvDownloadLink.setMarkupId("csv-" + mode.name().toLowerCase());
		add(csvDownloadLink);
	}

}
