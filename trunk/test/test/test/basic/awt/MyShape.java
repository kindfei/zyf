package test.basic.awt;

import java.awt.Graphics;

public class MyShape {
	private final static int[] round1 = {
		0, 0, 0, 1, 1, 1, 0, 0, 0,
		0, 1, 1, 0, 0, 0, 1, 1, 0,
		0, 1, 0, 0, 0, 0, 0, 1, 0,
		1, 0, 0, 0, 0, 0, 0, 0, 1,
		1, 0, 0, 0, 0, 0, 0, 0, 1,
		1, 0, 0, 0, 0, 0, 0, 0, 1,
		0, 1, 0, 0, 0, 0, 0, 1, 0,
		0, 1, 1, 0, 0, 0, 1, 1, 0,
		0, 0, 0, 1, 1, 1, 0, 0, 0,
	};
	
	public static void drawRound1(Graphics g, int x, int y) {
		int l = 9;
		for (int py = y, cy = 0; py < y + l; py++, cy++) {
			for (int px = x, cx = 0; px < x + l; px++, cx++) {
				if (round1[cy * l + cx] > 0)
					g.fillRect(px, py, 1, 1);
			}
		}
	}
	
	private final static int[] round2 = {
		0, 0, 1, 1, 1, 1, 0, 0,
		0, 1, 0, 0, 0, 0, 1, 0,
		1, 0, 0, 0, 0, 0, 0, 1,
		1, 0, 0, 0, 0, 0, 0, 1,
		1, 0, 0, 0, 0, 0, 0, 1,
		1, 0, 0, 0, 0, 0, 0, 1,
		0, 1, 0, 0, 0, 0, 1, 0,
		0, 0, 1, 1, 1, 1, 0, 0,
	};
	
	public static void drawRound2(Graphics g, int x, int y) {
		int l = 8;
		for (int py = y, cy = 0; py < y + l; py++, cy++) {
			for (int px = x, cx = 0; px < x + l; px++, cx++) {
				if (round2[cy * l + cx] > 0)
					g.fillRect(px, py, 1, 1);
			}
		}
	}
	
	private final static int[] round3 = {
		0, 0, 1, 1, 1, 0, 0,
		0, 1, 0, 0, 0, 1, 0,
		1, 0, 0, 0, 0, 0, 1,
		1, 0, 0, 0, 0, 0, 1,
		1, 0, 0, 0, 0, 0, 1,
		0, 1, 0, 0, 0, 1, 0,
		0, 0, 1, 1, 1, 0, 0,
	};
	
	public static void drawRound3(Graphics g, int x, int y) {
		int l = 7;
		for (int py = y, cy = 0; py < y + l; py++, cy++) {
			for (int px = x, cx = 0; px < x + l; px++, cx++) {
				if (round3[cy * l + cx] > 0)
					g.fillRect(px, py, 1, 1);
			}
		}
		
	}
}
