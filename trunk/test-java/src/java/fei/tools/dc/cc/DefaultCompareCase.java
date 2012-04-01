package fei.tools.dc.cc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fei.tools.dc.cc.RecordsGroupHelper.RecordsGroupingResult;
import fei.tools.dc.cc.cp.Reporter;




public class DefaultCompareCase extends AbstractCompareCase {
	
	private String name;
	
	private List<String> relatedColumns;
	
	private List<String> ignoreColumns;
	
	private Reporter reporter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRelatedColumns() {
		return relatedColumns;
	}

	public void setRelatedColumns(List<String> relatedColumns) {
		this.relatedColumns = relatedColumns;
	}

	public List<String> getIgnoreColumns() {
		return ignoreColumns;
	}

	public void setIgnoreColumns(List<String> ignoreColumns) {
		this.ignoreColumns = ignoreColumns;
	}

	public Reporter getReporter() {
		return reporter;
	}

	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}

	@Override
	public void compare(FetchResult fr1, FetchResult fr2) {
		List<Map<String, Object>> r1 = fr1.getRecords();
		List<Map<String, Object>> r2 = fr2.getRecords();
		
		RecordsGroupHelper recordsGroupHelper = new RecordsGroupHelper(relatedColumns);
		
		List<RecordsGroupingResult> grs1 = recordsGroupHelper.group(r1);
		List<RecordsGroupingResult> grs2 = recordsGroupHelper.group(r2);
		
		List<MatchingResult> mrs = new ArrayList<MatchingResult>();
		
		for (RecordsGroupingResult gr1 : grs1) {
			MatchingResult mr = new MatchingResult(fr1.getEnvName(), fr2.getEnvName(), gr1.getKeyValues());
			mr.setGroupingResult1(gr1);
			for (Iterator<RecordsGroupingResult> iterator = grs2.iterator(); iterator.hasNext();) {
				RecordsGroupingResult gr2 = iterator.next();
				if (gr2.getKeyValues().equals(gr1.getKeyValues())) {
					mr.setGroupingResult2(gr2);
					iterator.remove();
					break;
				}
			}
			mrs.add(mr);
		}
		
		for (RecordsGroupingResult gr : grs2) {
			MatchingResult mr = new MatchingResult(fr1.getEnvName(), fr2.getEnvName(), gr.getKeyValues());
			mr.setGroupingResult2(gr);
			
			mrs.add(mr);
		}
		
		List<CompareResult> compareResults = new ArrayList<CompareResult>();
		for (MatchingResult mr : mrs) {
			compareResults.add(mr.compare(ignoreColumns));
		}
		
		reporter.report(name, compareResults);
	}
}
