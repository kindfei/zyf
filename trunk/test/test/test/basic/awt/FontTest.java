package test.basic.awt;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FontTest {
	public static void main(String[] args) {
		FontFrame frame = new FontFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	// public static void main(String[] args)
	// {
	// String[] fontNames = GraphicsEnvironment
	// .getLocalGraphicsEnvironment()
	// .getAvailableFontFamilyNames();
	// for (int i = 0; i < fontNames.length; i++) {
	// System.out.println(fontNames[i]);
	// }
	// }
}

/**
 * A frame with a text message panel
 */
class FontFrame extends JFrame {
	public FontFrame() {
		setTitle("FontTest");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// add panel to frame

		FontPanel panel = new FontPanel();
		getContentPane().add(panel);
	}

	public static final int DEFAULT_WIDTH = 300;

	public static final int DEFAULT_HEIGHT = 200;
}

/**
 * A panel that shows a centered message in a box.
 */
class FontPanel extends JPanel {
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		String message = "aaaaaaa";

		Font f = new Font("MS PMincho", Font.BOLD, 36);
//		Font f = null;
//		try {
//			f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(
//					"C:/WINDOWS/Fonts/TIMES.TTF"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		g2.setFont(f.deriveFont(Font.BOLD, 36f));

		// measure the size of the message

		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(message, context);

		// set (x,y) = top left corner of text

		double x = (getWidth() - bounds.getWidth()) / 2;
		double y = (getHeight() - bounds.getHeight()) / 2;

		// add ascent to y to reach the baseline

		double ascent = -bounds.getY();
		double baseY = y + ascent;

		// draw the message

		//g2.drawString(message, (int) x, (int) baseY);
		PHPTinyFont.drawAsciiText(g2, message, (int) x, (int) baseY);

		g2.setPaint(Color.GRAY);

		// draw the baseline

		g2.draw(new Line2D.Double(x, baseY, x + bounds.getWidth(), baseY));

		// draw the enclosing rectangle

		Rectangle2D rect = new Rectangle2D.Double(x, y, bounds.getWidth(),
				bounds.getHeight());
		g2.draw(rect);
	}
}
