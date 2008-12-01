package test.jfreechart;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.VerticalAlignment;

public class Test extends ApplicationFrame {
	
	public Test(String title) {
		super(title);
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(170, 100));
		setContentPane(chartPanel);
	}
	
	private static XYDataset createDataset() {
		TimeSeries s1 = new TimeSeries("aaaaa", Second.class);
		s1.add(new Second(12, 23, 6, 1, 1, 2007), 181.8);
		s1.add(new Second(12, 12, 7, 1, 1, 2007), 167.3);
		s1.add(new Second(12, 11, 7, 1, 1, 2007), 153.8);
		s1.add(new Second(12, 23, 8, 1, 1, 2007), 167.6);
		s1.add(new Second(12, 23, 9, 1, 1, 2007), 158.8);
		s1.add(new Second(12, 25, 9, 1, 1, 2007), 148.3);
		s1.add(new Second(12, 2, 10, 1, 1, 2007), 153.9);
		s1.add(new Second(12, 12, 10, 1, 1, 2007), 142.7);
		s1.add(new Second(12, 23, 10, 1, 1, 2007), 123.2);
		s1.add(new Second(12, 1, 11, 1, 1, 2007), 131.8);
		s1.add(new Second(12, 12, 11, 1, 1, 2007), 139.6);
		s1.add(new Second(12, 1, 2, 2, 1, 2007), 142.9);
		s1.add(new Second(12, 23, 2, 2, 1, 2007), 138.7);
		s1.add(new Second(12, 21, 3, 2, 1, 2007), 137.3);
		s1.add(new Second(12, 50, 3, 2, 1, 2007), 143.9);
		s1.add(new Second(12, 12, 4, 2, 1, 2007), 139.8);
		s1.add(new Second(12, 22, 4, 2, 1, 2007), 137.0);
		s1.add(new Second(12, 23, 5, 2, 1, 2007), 132.8);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(s1);
		
		return dataset;
	}

	private static JFreeChart createChart(XYDataset dataset) {
		
		// create the chart...
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"", // title	
				"", // time axis label
				"", // value axis label											
				dataset, // data
			//	PlotOrientation.VERTICAL, 
				false, // include legend
				false, // tooltips
				false // urls
				);
		
		TextTitle t3 = new TextTitle("(C) KAZAKA Financial Group");
		t3.setFont(new Font("SansSerif", Font.BOLD, 6));
		t3.setPosition(RectangleEdge.BOTTOM);
		t3.setVerticalAlignment(VerticalAlignment.BOTTOM);
		t3.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		chart.addSubtitle(t3);
		
		TextTitle t1 = new TextTitle("USD/JPY       2007/01/01");
		t1.setFont(new Font("SansSerif", Font.PLAIN, 6));
		t1.setPosition(RectangleEdge.BOTTOM);
		t1.setVerticalAlignment(VerticalAlignment.TOP);
		t1.setHorizontalAlignment(HorizontalAlignment.LEFT);
		chart.addSubtitle(t1);
		
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);
		
		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(0.5, 0.5, 0.5, 0.5));
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setBasePaint(Color.blue);
		
		// Y
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRange(false);
		rangeAxis.setRange(100, 200);
		rangeAxis.setTickUnit(new NumberTickUnit(20));
		rangeAxis.setAxisLineVisible(false);
		rangeAxis.setTickMarksVisible(false);
		rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 6));
		
		// X
		DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
		domainAxis.setDateFormatOverride(new SimpleDateFormat("HH"));
		domainAxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
		SimpleDateFormat sdf = new SimpleDateFormat("HHddMMyyyy");
		try {
			domainAxis.setRange(sdf.parse("0601012007"), sdf.parse("0602012007"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		domainAxis.setTickUnit(new DateTickUnit(DateTickUnit.HOUR, 4));
		domainAxis.setAxisLineVisible(false);
		domainAxis.setTickMarksVisible(false);
		domainAxis.setTickLabelPaint(Color.BLACK);
		domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 7));
		
		try {
			ChartUtilities.saveChartAsJPEG(new File("E:/chart.jpg"), chart, 170, 100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		Test test = new Test("Line Chart Demo 2");
		test.pack();
		RefineryUtilities.centerFrameOnScreen(test);
		test.setVisible(true);
	}
}
