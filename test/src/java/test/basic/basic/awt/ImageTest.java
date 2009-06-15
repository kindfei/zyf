package test.basic.basic.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTest {
	BufferedImage image;
	Graphics2D g2;
	
	public void createImage() {
		image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
		g2 = image.createGraphics();
	}
	
	public void drawString(String message) {
		Font f = new Font("Serif", Font.BOLD, 12);
		g2.setFont(f);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, 200, 100);
		g2.setColor(Color.BLACK);
		
		g2.drawString(message, 10, 20);
	}
	
	public void drawString2(String message) {
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, 200, 100);
		g2.setColor(Color.BLACK);
		
		PHPTinyFont.drawAsciiText(g2, message, 10, 10);
	}
	
	public void drawShape(int x, int y) {
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, 200, 100);
		g2.setColor(Color.BLACK);
		
		MyShape.drawRound1(g2, 10, 10);
		MyShape.drawRound2(g2, 30, 10);
		MyShape.drawRound3(g2, 50, 10);
	}
	
	public void saveImage() {
		try {
			ImageIO.write(image, "png", new File("E:/test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ImageTest test = new ImageTest();
		test.createImage();
		test.drawShape(10,10);
		test.saveImage();
	}
}
