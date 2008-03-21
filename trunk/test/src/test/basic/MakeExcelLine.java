package test.basic;

public class MakeExcelLine {
	public static void main(String args[]) {
		
		int max = 256;
		
		MakeExcelLine a = new MakeExcelLine();
		if (max > 256) return;
		
		StringBuffer line = new StringBuffer();
		int temp = max/26;
		int left = max - temp*26;
		System.out.println(left);
		for(int i = 0; i < temp; i++) {
			for(int j = 1; j <= 26; j++) {
				line.append(a.transform(i) + a.transform(j) + "\t");
			}
		}
		if (left > 0) {
			for (int i = 1; i <= left; i++) {
				line.append(a.transform(temp) + a.transform(i) + "\t");
			}
		}
		
		System.out.println(line);
	}
	
	String transform(int i) {
		
		String args[] = {"","A","B","C","D","E","F",
				"G","H","I","J","K","L","M","N",
				"O","P","Q","R","S","T","U","V",
				"W","X","Y","Z"};
		return args[i];

	}
}
