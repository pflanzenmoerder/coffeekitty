package de.caffeine.kitty.web.caffeine;

import java.awt.Color;
import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import de.caffeine.kitty.service.CaffeineDTO;

/**
 * Model that generates a {@link JFreeChart} chart for display.
 */
public class TimeSeriesChartModel extends AbstractReadOnlyModel<JFreeChart> {
	private static final long serialVersionUID = 1L;
	
	private final IModel<List<CaffeineDTO>> chartDataModel;
	private final DateTickUnit dateTickUnit;
	
	public TimeSeriesChartModel(IModel<List<CaffeineDTO>> chartDataModel, DateTickUnit dateTickUnit) {
		this.chartDataModel = chartDataModel;
		this.dateTickUnit = dateTickUnit;
	}

	@Override
	public JFreeChart getObject() {
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
			null, 
			"time", 
			"caffeine level in mg", 
			createDataset(), 
			false, 
			false, 
			false);

		chart.setBackgroundPaint(Color.WHITE);

        final XYPlot plot = chart.getXYPlot();

		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.WHITE);

		final XYItemRenderer renderer = plot.getRenderer();
		if (renderer instanceof StandardXYItemRenderer) {
			final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
			rr.setPlotImages(false);
			rr.setPlotLines(true);
			rr.setSeriesShapesFilled(0, false);
		}

		final DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
		dateAxis.setTickUnit(dateTickUnit);
		
		return chart;
	}

	private XYDataset createDataset() {
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		if (chartDataModel != null && chartDataModel.getObject() != null) {
			final TimeSeries s1 = new TimeSeries("caffeine", null, null);

			for (final CaffeineDTO caffeineLevel : chartDataModel.getObject()) {
				s1.add(new Minute(caffeineLevel.time), caffeineLevel.level);
			}
			dataset.addSeries(s1);
		}
		return dataset;
	}

}
