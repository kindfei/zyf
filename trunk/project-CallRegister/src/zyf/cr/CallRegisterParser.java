package zyf.cr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CallRegisterParser {
	private static Log log = LogFactory.getLog(CallRegisterParser.class);
	private SimpleDateFormat sdf;
	private String num;
	private String pwd;
	private String bDate;
	private String eDate;
	private String outputDir = "./";
	
	public static void main(String[] args) {
		CallRegisterParser parser = new CallRegisterParser();
		try {
			parser.process();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public CallRegisterParser() {
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if (PropertyUtils.isConsoleInput()) {
			num = CMDHelper.input("Input phone number");
			pwd = CMDHelper.input("Input password");
			int opt = CMDHelper.option(new String[] {"Input Recent Months To Search", "Input Dates Manual To Search"});
			switch (opt) {
			case 0:
				int months = 0;
				while (true) {
					try {
						months = Integer.parseInt(CMDHelper.input("Input how many recent months that you wanna search"));
						break;
					} catch (NumberFormatException e) {
						System.out.println("Input number please!");
					}
				}
				bDate = sdf.format(getPrevMonths(months - 1));
				eDate = sdf.format(getMaxDate());
				break;
			case 1:
				Date beginDate = inputDate("Input begin date", getMinDate(), getMaxDate());
				Date endDate = inputDate("Input end date", beginDate, getMaxDate());
				bDate = sdf.format(beginDate);
				eDate = sdf.format(endDate);
				break;
			default:
				break;
			}
		} else {
			num = PropertyUtils.getNumber();
			pwd = PropertyUtils.getPassword();
			int months = PropertyUtils.getRecentlyMonths();
			if (months > 0) {
				bDate = sdf.format(getPrevMonths(months - 1));
				eDate = sdf.format(getMaxDate());
			} else {
				bDate = PropertyUtils.getBeginDate();
				eDate = PropertyUtils.getEndDate();
			}
		}
		
		outputDir = PropertyUtils.getSavePath();
		if (!outputDir.endsWith("/")) outputDir += "/";
		File dir = new File(outputDir);
		if (!dir.exists()) dir.mkdirs();
		
		log.info("Phone Number:" + num);
		log.info("Password:" + pwd);
		log.info("Begin Date:" + bDate);
		log.info("End Date:" + eDate);
		log.info("Save Path:" + outputDir);
	}
	
	private Date getMinDate() {
		return getPrevMonths(5);
	}
	
	private Date getPrevMonths(int months) {
		if (months > 5) months = 5;
		if (months < 0) months = 0;
		months *= -1;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, months);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	private Date getMaxDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	private Date inputDate(String msg, Date minDate, Date maxDate) {
		String strRange = "(" + sdf.format(minDate) + " to " + sdf.format(maxDate) + ")";
		Date inputDate = null;
		while(true) {
			String strDate = CMDHelper.input(msg + strRange);
			try {
				inputDate = sdf.parse(strDate);
				if (inputDate.compareTo(minDate) >= 0 && inputDate.compareTo(maxDate) <= 0) break;
				else System.out.println("error: Input date must between" + strRange);
			} catch (ParseException e) {
				System.out.println("error: Input format wrong");
			}
		}
		return inputDate;
	}
	
	private void process() throws HttpException, IOException {
		log.info("Call Register Parser process begin.");
		HttpHandler httpHandler = new HttpHandler(num, pwd, bDate, eDate);
		ResultInfo result = httpHandler.process();
		timesOrder(result.getCalls(), "Call");
		timesOrder(result.getMessages(), "Message");
		log.info("Call Register Parser process end.");
	}
	
	private void timesOrder(RegisterInfo[] beans, String mode) throws IOException {
		HashMap map = new HashMap();
		for (int i = 0; i < beans.length; i++) {
			String key = beans[i].getNumber();
			if (key.length() == 12  && key.startsWith("0411")) {
				key = key.substring(4);
			} else if (key.length() == 13 && key.startsWith("86")) {
				key = key.substring(2);
			}
			List list = (ArrayList) map.get(key);
			if (list == null) {
				list = new ArrayList();
				map.put(key, list);
			}
			list.add(beans[i]);
		}
		
		TreeSet set = new TreeSet(new Comparator() {
			public int compare(Object o1, Object o2) {
				int s1 = ((List)((Map.Entry)o1).getValue()).size();
				int s2 = ((List)((Map.Entry)o2).getValue()).size();
				if (s1 > s2) return -1;
				return 1;
			}
		});
		
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			set.add(iter.next());
		}
		
		File file = new File(outputDir + num + " " + mode + " from " + bDate + " to " + eDate + " (Times Order).txt");
		if (!file.exists()) file.createNewFile();
		FileWriter fw = new FileWriter(file, false);
		PrintWriter writer = new PrintWriter(fw);
		
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			List element = (List) entry.getValue();
			writer.println("Number:" + key + "\tTimes:" + element.size());
			for (Iterator iterator = element.iterator(); iterator.hasNext();) {
				RegisterInfo bean = (RegisterInfo) iterator.next();
				writer.print("\t");
				writer.println(bean.toString());
			}
			writer.flush();
		}
		
		writer.close();
		fw.close();
	}
}
