package fei.tools.dc.cc;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fei.tools.dc.util.GroupHelper;




public class RecordsGroupHelper extends GroupHelper<Map<String, Object>, RecordsGroupHelper.RecordsGroupingResult> {

	private static final Log log = LogFactory.getLog(RecordsGroupHelper.class);
	
	public RecordsGroupHelper(List<String> relatedColumns) {
		super(relatedColumns);
	}
	
	@Override
	protected Object getValue(Map<String, Object> element, String key) {
		if (!element.containsKey(key)) {
			log.warn(key + " was not contained in the record. record=" + element.toString());
		}
		return element.get(key);
	}

	@Override
	protected RecordsGroupingResult newGroupingResult() {
		return new RecordsGroupingResult();
	}
	
	public static class RecordsGroupingResult extends GroupHelper.GroupingResult<Map<String, Object>> {
		
	}
}
