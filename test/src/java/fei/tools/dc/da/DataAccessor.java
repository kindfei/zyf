package fei.tools.dc.da;

import java.util.List;
import java.util.Map;

public interface DataAccessor {
	List<Map<String, Object>> fetch(QueryInfo queryInfo);
	String getEnvName();
}
