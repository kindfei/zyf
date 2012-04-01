package fei.tools.dc.cc;

import java.util.ArrayList;
import java.util.List;

public class CompareResult {
	private MatchingResult matchingResult;
	
	private boolean same;
	
	private List<RowCompareResult> rowCompareResults = new ArrayList<RowCompareResult>();
	
	public CompareResult() {
		super();
	}

	public CompareResult(MatchingResult matchingResult, boolean same, List<RowCompareResult> rowCompareResults) {
		super();
		this.matchingResult = matchingResult;
		this.same = same;
		this.rowCompareResults = rowCompareResults;
	}

	public MatchingResult getMatchingResult() {
		return matchingResult;
	}

	public void setMatchingResult(MatchingResult matchingResult) {
		this.matchingResult = matchingResult;
	}

	public boolean isSame() {
		return same;
	}

	public void setSame(boolean same) {
		this.same = same;
	}
	
	public List<RowCompareResult> getRowCompareResults() {
		return rowCompareResults;
	}

	public void setRowCompareResults(List<RowCompareResult> rowCompareResults) {
		this.rowCompareResults = rowCompareResults;
	}

	public static class RowCompareResult {
		private List<ColumnCompareResult> columnCompareResults = new ArrayList<ColumnCompareResult>();

		public RowCompareResult() {
			super();
		}

		public RowCompareResult(List<ColumnCompareResult> columnCompareResults) {
			super();
			this.columnCompareResults = columnCompareResults;
		}

		public List<ColumnCompareResult> getColumnCompareResults() {
			return columnCompareResults;
		}

		public void setColumnCompareResults(
				List<ColumnCompareResult> columnCompareResults) {
			this.columnCompareResults = columnCompareResults;
		}
	}

	public static class ColumnCompareResult {
		private String columnName;
		private Object value1;
		private Object value2;
		private ResultEnum result;
		
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		public Object getValue1() {
			return value1;
		}
		public void setValue1(Object value1) {
			this.value1 = value1;
		}
		public Object getValue2() {
			return value2;
		}
		public void setValue2(Object value2) {
			this.value2 = value2;
		}
		public ResultEnum getResult() {
			return result;
		}
		public void setResult(ResultEnum result) {
			this.result = result;
		}
	}
}
