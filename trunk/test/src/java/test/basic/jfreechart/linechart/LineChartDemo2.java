package test.basic.jfreechart.linechart;

import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.
 * <p>
 * IMPORTANT NOTE: THIS DEMO IS DOCUMENTED IN THE JFREECHART DEVELOPER GUIDE. DO
 * NOT MAKE CHANGES WITHOUT UPDATING THE GUIDE ALSO!!
 */
public class LineChartDemo2 extends ApplicationFrame {
	/**
	 * Creates a new demo.
	 * 
	 * @param title
	 *            the frame title.
	 */
	public LineChartDemo2(String title) {
		super(title);
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}

	/**
	 * Creates a sample dataset.
	 * 
	 * @return a sample dataset.
	 */
	private static XYDataset createDataset() {
		XYSeries series1 = new XYSeries("First");
		series1.add(1.1, 1.2);
		series1.add(1.2, 4.3);
		series1.add(1.3, 3.4);
		series1.add(4.3, 5.0);
		series1.add(5.4, 5.2);
		series1.add(6.1, 7.3);
		series1.add(7.2, 7.3);
		series1.add(8.1, 8.6);
		
//		XYSeries series2 = new XYSeries("Second");
//		series2.add(1.0, 5.0);
//		series2.add(2.0, 7.0);
//		series2.add(3.0, 6.0);
//		series2.add(4.0, 8.0);
//		series2.add(5.0, 4.0);
//		series2.add(6.0, 4.0);
//		series2.add(7.0, 2.0);
//		series2.add(8.0, 1.0);
//		
//		XYSeries series3 = new XYSeries("Third");
//		series3.add(3.0, 4.0);
//		series3.add(4.0, 3.0);
//		series3.add(5.0, 2.0);
//		series3.add(6.0, 3.0);
//		series3.add(7.0, 6.0);
//		series3.add(8.0, 3.0);
//		series3.add(9.0, 4.0);
//		series3.add(10.0, 3.0);
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
//		dataset.addSeries(series2);
//		dataset.addSeries(series3);
		
		return dataset;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the data for the chart.
	 * 
	 * @return a chart.
	 */
	private static JFreeChart createChart(XYDataset dataset) {
		
		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Line Chart Demo 2", // title	
				"", // x axis label
				"", // y axis label											
				dataset, // data
				PlotOrientation.VERTICAL, 
				true, // include legend
				true, // tooltips
				false // urls
				);
		
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);
		
		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		
//		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
//		renderer.setShapesVisible(true);
//		renderer.setShapesFilled(true);
		
		NumberAxis rangeAxis = null;
		
		// change the auto tick unit selection to integer units only...
		rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRange(false);
		rangeAxis.setRange(0, 15);
		rangeAxis.setTickUnit(new NumberTickUnit(5));
		rangeAxis.setAxisLineVisible(false);
		rangeAxis.setTickMarksVisible(false);
		
		rangeAxis = (NumberAxis) plot.getDomainAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRange(false);
		rangeAxis.setRange(0, 20);
		rangeAxis.setTickUnit(new NumberTickUnit(2));
		rangeAxis.setAxisLineVisible(false);
		rangeAxis.setTickMarksVisible(false);
		
		// OPTIONAL CUSTOMISATION COMPLETED.
		return chart;
	}

	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 * 
	 * @return A panel.
	 */
	public static JPanel createDemoPanel() {
		JFreeChart chart = createChart(createDataset());
		return new ChartPanel(chart);
	}

	/**
	 * Starting point for the demonstration application.
	 * 
	 * @param args
	 *            ignored.
	 */
	public static void main(String[] args) {
		LineChartDemo2 demo = new LineChartDemo2("Line Chart Demo 2");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
}
