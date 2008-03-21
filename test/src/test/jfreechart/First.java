package test.jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class First {
	public static void main(String[] args) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Category 1", 43.2);
		dataset.setValue("Category 2", 27.9);
		dataset.setValue("Category 3", 79.5);
		
		JFreeChart chart = ChartFactory.createPieChart("Sample Pie Chart", dataset, true, true, false);
		
		ChartFrame frame = new ChartFrame("First", chart);
		frame.pack();
		frame.setVisible(true);
	}
}
