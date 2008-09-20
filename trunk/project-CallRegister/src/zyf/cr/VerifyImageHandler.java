package zyf.cr;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VerifyImageHandler {
	private static Log log = LogFactory.getLog(VerifyImageHandler.class);
	private int unitX = 10;
	private int unitY = 15;
	private int conut = 4;
	private int min = 1;
	private int maxMiss = 20;
	private char fill = '0';
	private char unfill = ' ';
	private String fileType = ".txt";
	private String saveDir = "./ImageResult/";
	private SampleInfo[] samples;
	
	public VerifyImageHandler() {
		saveDir = PropertyUtils.getImageSavePath();
		if (!saveDir.endsWith("/")) saveDir += "/";
		File dir = new File(saveDir);
		if (!dir.exists()) dir.mkdirs();
	}
	
	public static void main(String[] args) {
		log.info("VerifyImageHandler study begin.");
		VerifyImageHandler inst = new VerifyImageHandler();
		HttpHandler httpHandler = new HttpHandler();
		
		try {
			while (true) {
				inst.study(httpHandler.getImage());
				if (!CMDHelper.confirm("Whether to continue to study"))
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info("VerifyImageHandler study end.");
	}
	
	public void study(InputStream in) throws IOException {
		UnitInfo[] units = analyse(in);
		for (int i = 0; i < units.length; i++) {
			char[] unitAry = units[i].getArray();
			int width = units[i].getWidth();
			int height = units[i].getHeight();
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < width; k++) {
					System.out.print(unitAry[j + (k * height)]);
				}
				System.out.println();
			}
			File file = new File(saveDir + CMDHelper.input("What is this?") + fileType);
			if (!file.exists()) file.createNewFile();
			FileWriter writer = new FileWriter(file, true);
			writer.write(new String(unitAry));
			writer.write("\n");
			writer.flush();
			writer.close();
		}
	}
	
	public String parse(InputStream in) throws IOException {
		if (samples == null) initSamples();
		UnitInfo[] units = analyse(in);
		String result = "";
		for (int i = 0; i < units.length; i++) {
			char[] unitAry = units[i].getArray();
			CompareResult cr = compare(unitAry, 0, 0);
			
			if (cr.getMiss() > maxMiss) {
				TreeSet set = new TreeSet(new Comparator () {
					public int compare(Object obj1, Object obj2) {
						CompareResult cr1 = (CompareResult) obj1;
						CompareResult cr2 = (CompareResult) obj2;
						if (cr1.getHit() < cr2.getHit()) {
							return -1;
						} else if (cr1.getHit() > cr2.getHit()) {
							return 1;
						}
						return 0;
					}
				});
				set.add(cr);
				set.add(compare(unitAry, 0, 1));
				set.add(compare(unitAry, 1, 0));
				cr = (CompareResult) set.last();
			}
			log.info(cr.getValue() + " - " + cr.getHit() + "/" + (cr.getHit() + cr.getMiss()) + " miss:" + cr.getMiss());
			result += cr.getValue();
		}
		
		if (!CMDHelper.confirm("verify code = " + result)) {
			result = CMDHelper.input("input verify code: ");
		}
		
		return result;
	}
	
	private CompareResult compare(char[] unitAry, int sScale, int uScale) {
		int maxHit = 0;
		int minMiss = 0;
		String value = null;
		for (int i = 0; i < samples.length; i++) {
			char[] sampleAry = samples[i].getArray();
			int sAdjust = sScale * unitY;
			int uAdjust = uScale * unitY;
			int sLen = sampleAry.length - sAdjust;
			int uLen = unitAry.length - uAdjust;
			int len = sLen < uLen ? sLen : uLen;
			
			int hit = 0;
			int miss = 0;
			for (int j = 0; j < len; j++) {
				if (unitAry[j + uAdjust] == sampleAry[j + sAdjust]) hit += 1;
				else miss += 1;
			}
			
			if (hit > maxHit) {
				maxHit = hit;
				minMiss = miss;
				value = samples[i].getValue();
			}
		}
		return new CompareResult(value, maxHit, minMiss);
	}
	
	private void initSamples() throws IOException {
		File dir = new File(saveDir);
		File[] files = dir.listFiles();
		samples = new SampleInfo[files.length];
		for (int i = 0; i < files.length; i++) {
			BufferedReader reader = new BufferedReader(new FileReader(files[i]));
			int[] vertCntAry = null;
			int count = 0;
			for (String str = reader.readLine(); str != null; str = reader.readLine()) {
				char[] array = str.toCharArray();
				if (vertCntAry == null) vertCntAry = new int[array.length];
				for (int j = 0; j < array.length; j++) {
					if (array[j] == fill) vertCntAry[j] += 1;
				}
				count++;
			}

			char[] sampleAry = new char[vertCntAry.length];
			for (int j = 0; j < vertCntAry.length; j++) {
				if (vertCntAry[j] >= count / 2) sampleAry[j] = fill;
				else sampleAry[j] = unfill;
			}
			samples[i] = new SampleInfo(sampleAry, files[i].getName().replaceAll(fileType, ""));
			reader.close();
		}
	}
	
	private UnitInfo[] analyse(InputStream in) throws IOException {
		char[][] imgAry = readImage(in);
		int width = imgAry.length;
		int height = imgAry[0].length;
		int[] vertCntAry = new int[height];
		int[] horCntAry = new int[width];
		UnitInfo[] units = new UnitInfo[conut];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				char i = imgAry[x][y];
				if (i == fill) {
					vertCntAry[y] += 1;
				}
			}
		}

		int yb = 0;
		int ye = 0;
		for (int i = 0; i < vertCntAry.length; i++) {
			int n = vertCntAry[i];
			if (n != 0) {
				if (n > vertCntAry[i + unitY]) {
					yb = i;
					ye = i + unitY;
					break;
				}
			}
		}
		
		for (int x = 0; x < width; x++) {
			for (int y = yb; y < ye; y++) {
				char i = imgAry[x][y];
				if (i == fill) {
					horCntAry[x] += 1;
				}
			}
		}
		
		int index = 0;
		for (int i = 0; i < horCntAry.length; i++) {
			if (index >= conut) break;
			int n = horCntAry[i];
			if (n > min) {
				int xe = i + unitX;
				for (int j = i; j < i + unitX; j++) {
					if (horCntAry[j] == 0) {
						xe = j;
						break;
					}
				}
				units[index++] = createNumber(imgAry, i, xe, yb, ye);
				i = xe - 1;
			}
		}
		
		return units;
	}
	
	private char[][] readImage(InputStream in) throws IOException {
		BufferedImage image = ImageIO.read(in);
		int width = image.getWidth();
		int height = image.getHeight();
		
		char[][] imgAry = new char[width][height];
		StringBuffer sb = new StringBuffer("\n");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (image.getRGB(x, y) < -14000000) {
					imgAry[x][y] = fill;
					sb.append(fill);
				} else { 
					imgAry[x][y] = unfill;
					sb.append(unfill);
				}
			}
			sb.append("\n");
		}
		log.info(sb.toString());
		return imgAry;
	}
	
	private UnitInfo createNumber(char[][] imgAry, int xb, int xe, int yb, int ye) {
		int width = xe - xb;
		int height = ye - yb;
		char[] unitAry = new char[width * height];
		int index = 0;
		for (int x = xb; x < xe; x++) {
			for (int y = yb; y < ye; y++) {
				unitAry[index++] = imgAry[x][y];
			}
		}
		return new UnitInfo(unitAry, width, height);
	}
}

class UnitInfo {
	private char[] array;
	private int width;
	private int height;
	public UnitInfo(char[] array, int width, int height) {
		this.array = array;
		this.width = width;
		this.height = height;
	}
	public char[] getArray() {
		return array;
	}
	public void setArray(char[] array) {
		this.array = array;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
}

class SampleInfo {
	private char[] array;
	private String value;
	public SampleInfo(char[] array, String value) {
		this.array = array;
		this.value = value;
	}
	public char[] getArray() {
		return array;
	}
	public void setArray(char[] array) {
		this.array = array;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}

class CompareResult {
	private String value;
	private int hit;
	private int miss;
	public CompareResult(String value, int hit, int miss) {
		this.value = value;
		this.hit = hit;
		this.miss = miss;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public int getMiss() {
		return miss;
	}
	public void setMiss(int miss) {
		this.miss = miss;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
