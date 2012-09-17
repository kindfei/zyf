package fei.tools.dc.cc.cp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fei.tools.dc.cc.CompareResult;
import fei.tools.dc.cc.ResultEnum;
import fei.tools.dc.cc.CompareResult.ColumnCompareResult;
import fei.tools.dc.cc.CompareResult.RowCompareResult;
import fei.tools.dc.cc.cp.CsvFileReporter.CriGroupHelper.CriGroupingResult;
import fei.tools.dc.util.GroupHelper;




public class CsvFileReporter extends FileReporter {
	private static final Log log = LogFactory.getLog(CsvFileReporter.class);

	private static final String FILE_NAME_SUFFIX = ".csv";
	private static final String COMMA = ",";
	private static final String NEWLINE = System.getProperty("line.separator");
	private static final String EMPTY_STRING = "";
	private static final String SPACE = " ";
	private static final String ENV_NAME_TITLE = EMPTY_STRING;
	private static final String RESULT_TITLE = "result";
	
	private String nullValue = "NULL";
	
	private boolean ignoreSameResult = true;
	
	private boolean onlyDifferentColumn = false;
	
	private List<String> columnsForDisplay;
	
	private PresentMode presentMode = PresentMode.horizontal;
	
	private List<String> groupKeys;
	
	private boolean inOneFile = false;

	@Override
	public void report(String name, List<CompareResult> compareResults) {
		List<CompareResultInfo> cris = new ArrayList<CsvFileReporter.CompareResultInfo>();
		
		for (CompareResult cr : compareResults) {
			if (ignoreSameResult && cr.isSame()) {
				continue;
			}
			
			StringBuilder content;
			
			if (presentMode == PresentMode.horizontal) {
				content = buildHorizontalContent(cr);
			} else if (presentMode == PresentMode.vertical) {
				content = buildVerticalContent(cr);
			} else {
				throw new IllegalArgumentException("Unknown presentMode. presentMode=" + presentMode);
			}
			
			cris.add(new CompareResultInfo(cr, cr.getMatchingResult().getKeyValues().toString(), content.toString()));
		}
		
		CriGroupHelper criGroupHelper = new CriGroupHelper(groupKeys);
		List<CriGroupingResult> groupingResult = criGroupHelper.group(cris);
		
		try {
			output(name, groupingResult);
		} catch (IOException e) {
			log.error("Result of compare report error.", e);
		}
	}
	
	private StringBuilder buildHorizontalContent(CompareResult cr) {
		StringBuilder content = new StringBuilder();
		
		for (RowCompareResult rcr : cr.getRowCompareResults()) {
			StringBuilder row1 = new StringBuilder(ENV_NAME_TITLE);
			StringBuilder row2 = new StringBuilder(cr.getMatchingResult().getEnvName1());
			StringBuilder row3 = new StringBuilder(cr.getMatchingResult().getEnvName2());
			StringBuilder row4 = new StringBuilder(RESULT_TITLE);
			
			for (ColumnCompareResult ccr : rcr.getColumnCompareResults()) {
				if (displayOrNot(ccr)) {
					continue;
				}
				row1.append(COMMA).append(ccr.getColumnName());
				row2.append(COMMA).append(stringValue(ccr.getValue1()));
				row3.append(COMMA).append(stringValue(ccr.getValue2()));
				row4.append(COMMA).append(ccr.getResult().getDisplayName());
			}
			
			content.append(row1).append(NEWLINE);
			content.append(row2).append(NEWLINE);
			content.append(row3).append(NEWLINE);
			content.append(row4).append(NEWLINE).append(NEWLINE);
		}
		
		return content;
	}
	
	private StringBuilder buildVerticalContent(CompareResult cr) {
		List<StringBuilder> rows = new ArrayList<StringBuilder>();
		
		for (RowCompareResult rcr : cr.getRowCompareResults()) {
			
			StringBuilder row0 = obtainRow(rows, 0);
			row0.append(ENV_NAME_TITLE);
			row0.append(COMMA).append(cr.getMatchingResult().getEnvName1());
			row0.append(COMMA).append(cr.getMatchingResult().getEnvName2());
			row0.append(COMMA).append(RESULT_TITLE);
			
			int size = rcr.getColumnCompareResults().size();
			Iterator<ColumnCompareResult> iter = rcr.getColumnCompareResults().iterator();
			for (int i = 1; i < size + 1;) {
				if (iter.hasNext()) {
					ColumnCompareResult ccr = iter.next();
					if (displayOrNot(ccr)) {
						continue;
					}
					StringBuilder rowx = obtainRow(rows, i);
					rowx.append(ccr.getColumnName());
					rowx.append(COMMA).append(stringValue(ccr.getValue1()));
					rowx.append(COMMA).append(stringValue(ccr.getValue2()));
					rowx.append(COMMA).append(ccr.getResult().getDisplayName());
					i++;
				} else {
					StringBuilder rowx = obtainRow(rows, i);
					rowx.append(EMPTY_STRING);
					rowx.append(COMMA).append(EMPTY_STRING);
					rowx.append(COMMA).append(EMPTY_STRING);
					rowx.append(COMMA).append(EMPTY_STRING);
					i++;
				}
			}
		}
		
		StringBuilder content = new StringBuilder();
		for (StringBuilder sb : rows) {
			String row = sb.toString();
			if (!row.replaceAll(COMMA, EMPTY_STRING).trim().isEmpty()) {
				content.append(row).append(NEWLINE);
			}
		}
		
		return content;
	}
	
	private StringBuilder obtainRow(List<StringBuilder> sbs, int index) {
		StringBuilder sb;
		if (sbs.size() <= index) {
			sb = new StringBuilder();
			sbs.add(sb);
		} else {
			sb = sbs.get(index);
			sb.append(COMMA).append(EMPTY_STRING).append(COMMA);
		}
		return sb;
	}
	
	private String stringValue(Object obj) {
		return obj == null ? nullValue : String.valueOf(obj).replaceAll(COMMA, SPACE);
	}
	
	private boolean displayOrNot(ColumnCompareResult ccr) {
		return onlyDifferentColumn && ccr.getResult() != ResultEnum.different
				&& (columnsForDisplay == null || !columnsForDisplay.contains(ccr.getColumnName()));
	}
	
	private void output(String name, List<CriGroupingResult> groupingResult) throws IOException {
		FileWriter writer = null;
		try {
			for (CriGroupingResult cgi : groupingResult) {
				File file;
				if (inOneFile) {
					file = new File(getTargetDir() + File.separator + name + "(" + currentTime() + ")" + FILE_NAME_SUFFIX);
				} else {
					file = new File(getTargetDir() + File.separator + name + cgi.getKeyValues().toString() + FILE_NAME_SUFFIX);
					if (file.exists()) file.delete();
				}
				
				file.createNewFile();
				
				writer = new FileWriter(file, true);
				
				for (CompareResultInfo cri : cgi.getElements()) {
					writer.write(cri.getTitle());
					writer.write(NEWLINE);
					writer.write(cri.getContent());
					writer.write(NEWLINE);
				}
				
				writer.flush();
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	private String currentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	static class CompareResultInfo {
		private CompareResult compareResult;
		private String title;
		private String content;
		
		public CompareResultInfo(CompareResult compareResult, String title, String content) {
			super();
			this.compareResult = compareResult;
			this.title = title;
			this.content = content;
		}
		
		public CompareResult getCompareResult() {
			return compareResult;
		}
		public void setCompareResult(CompareResult compareResult) {
			this.compareResult = compareResult;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getTitle() {
			return title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
	
	static class CriGroupHelper extends GroupHelper<CompareResultInfo, CriGroupHelper.CriGroupingResult> {
		
		public CriGroupHelper(List<String> groupKeys) {
			super(groupKeys);
		}
		
		@Override
		protected Object getValue(CompareResultInfo element, String key) {
			Map<String, Object> keyValues = element.getCompareResult().getMatchingResult().getKeyValues();
			if (!keyValues.containsKey(key)) {
				log.warn(key + " was not contained in the keyValues. keyValues=" + element.toString());
			}
			return keyValues.get(key);
		}

		@Override
		protected CriGroupingResult newGroupingResult() {
			return new CriGroupingResult();
		}

		static class CriGroupingResult extends GroupHelper.GroupingResult<CompareResultInfo> {
			
		}
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public boolean isIgnoreSameResult() {
		return ignoreSameResult;
	}

	public void setIgnoreSameResult(boolean ignoreSameResult) {
		this.ignoreSameResult = ignoreSameResult;
	}

	public boolean isOnlyDifferentColumn() {
		return onlyDifferentColumn;
	}

	public void setOnlyDifferentColumn(boolean onlyDifferentColumn) {
		this.onlyDifferentColumn = onlyDifferentColumn;
	}

	public List<String> getColumnsForDisplay() {
		return columnsForDisplay;
	}

	public void setColumnsForDisplay(List<String> columnsForDisplay) {
		this.columnsForDisplay = columnsForDisplay;
	}

	public PresentMode getPresentMode() {
		return presentMode;
	}

	public void setPresentMode(PresentMode presentMode) {
		this.presentMode = presentMode;
	}

	public List<String> getGroupKeys() {
		return groupKeys;
	}

	public void setGroupKeys(List<String> groupKeys) {
		this.groupKeys = groupKeys;
	}

	public boolean isInOneFile() {
		return inOneFile;
	}

	public void setInOneFile(boolean inOneFile) {
		this.inOneFile = inOneFile;
	}

}
