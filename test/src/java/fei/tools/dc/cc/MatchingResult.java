package fei.tools.dc.cc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fei.tools.dc.cc.CompareResult.ColumnCompareResult;
import fei.tools.dc.cc.CompareResult.RowCompareResult;
import fei.tools.dc.cc.RecordsGroupHelper.RecordsGroupingResult;




public class MatchingResult {
	private String envName1;
	private String envName2;
	
	private Map<String, Object> keyValues;
	
	private RecordsGroupingResult groupingResult1;
	private RecordsGroupingResult groupingResult2;
	
	public MatchingResult() {
		super();
	}
	
	public MatchingResult(String envName1, String envName2, Map<String, Object> keyValues) {
		super();
		this.envName1 = envName1;
		this.envName2 = envName2;
		this.keyValues = keyValues;
	}

	public String getEnvName1() {
		return envName1;
	}
	public void setEnvName1(String envName1) {
		this.envName1 = envName1;
	}
	public String getEnvName2() {
		return envName2;
	}
	public void setEnvName2(String envName2) {
		this.envName2 = envName2;
	}
	public Map<String, Object> getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(Map<String, Object> keyValues) {
		this.keyValues = keyValues;
	}
	public RecordsGroupingResult getGroupingResult1() {
		return groupingResult1;
	}
	public void setGroupingResult1(RecordsGroupingResult groupingResult1) {
		this.groupingResult1 = groupingResult1;
	}
	public RecordsGroupingResult getGroupingResult2() {
		return groupingResult2;
	}
	public void setGroupingResult2(RecordsGroupingResult groupingResult2) {
		this.groupingResult2 = groupingResult2;
	}
	
	public CompareResult compare(List<String> ignoreColumns) {
		
		List<RowCompareResult> rowCompareResults = new ArrayList<RowCompareResult>();
		boolean same = true;
		
		Iterator<Map<String, Object>> iter1 = null;
		Iterator<Map<String, Object>> iter2 = null;

		if (groupingResult1 != null) iter1 = groupingResult1.getElements().iterator();
		if (groupingResult2 != null) iter2 = groupingResult2.getElements().iterator();

		while ((iter1 != null && iter1.hasNext()) || (iter2 != null && iter2.hasNext())) {
			
			List<ColumnCompareResult> columnCompareResults = new ArrayList<ColumnCompareResult>();
			
			Map<String, Object> r1 = null;
			Map<String, Object> r2 = null;
			if (iter1 != null && iter1.hasNext()) r1 = iter1.next();
			if (iter2 != null && iter2.hasNext()) r2 = iter2.next();
			
			Iterator<Entry<String, Object>> i1 = null;
			Iterator<Entry<String, Object>> i2 = null;

			if (r1 != null) i1 = r1.entrySet().iterator();
			if (r2 != null) i2 = r2.entrySet().iterator();
			
			
			while ((i1 != null && i1.hasNext()) || (i2 != null && i2.hasNext())) {
				ColumnCompareResult ccr = new ColumnCompareResult();
				
				Entry<String, Object> e1 = null;
				Entry<String, Object> e2 = null;
				if (i1 != null && i1.hasNext()) e1 = i1.next();
				if (i2 != null && i2.hasNext()) e2 = i2.next();
				
				String columnName = null;
				Object value1 = null;
				Object value2 = null;
				
				if (e1 != null) {
					columnName = e1.getKey();
					value1 = e1.getValue();
				} else {
					columnName = e2.getKey();
				}

				if (e2 != null) {
					value2 = e2.getValue();
				}
				
				ccr.setColumnName(columnName);
				ccr.setValue1(value1);
				ccr.setValue2(value2);
				
				if (ignoreColumns != null && ignoreColumns.contains(columnName)) {
					ccr.setResult(ResultEnum.ignore);
				} else {
					if (value1 == value2) {
						ccr.setResult(ResultEnum.same);
					} else if (value1 == null) {
						ccr.setResult(ResultEnum.different);
						same = false;
					} else if (value1.equals(value2)) {
						ccr.setResult(ResultEnum.same);
					} else {
						ccr.setResult(ResultEnum.different);
						same = false;
					}
				}
				
				columnCompareResults.add(ccr);
			}
			
			rowCompareResults.add(new RowCompareResult(columnCompareResults));
		}
		
		return new CompareResult(this, same, rowCompareResults);
	}
}
