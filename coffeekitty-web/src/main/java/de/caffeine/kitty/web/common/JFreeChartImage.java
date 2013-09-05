package de.caffeine.kitty.web.common;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.WebResponse.CacheScope;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.util.time.Duration;
import org.jfree.chart.JFreeChart;

@SuppressWarnings("serial")
public class JFreeChartImage extends Image {

	private int width;
	private int height;

	public JFreeChartImage(String id, IModel<JFreeChart> chartModel, int width, int height){
		super(id, chartModel);
		this.width = width;
		this.height = height;
	}

	@Override
	protected IResource getImageResource() {
		final DynamicImageResource resource = new DynamicImageResource() {

			@Override
			protected byte[] getImageData(final Attributes attributes) {
				JFreeChart chart = (JFreeChart) getDefaultModelObject();
				return toImageData(chart.createBufferedImage(width, height));
			}

			@Override
			protected void configureResponse(final ResourceResponse response, final Attributes attributes) {
				super.configureResponse(response, attributes);

				response.setCacheDuration(Duration.NONE);
				response.setCacheScope(CacheScope.PRIVATE);
			}

		};

		return resource;
	}
}