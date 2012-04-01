package fei.tools.dc.da;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class SpringJdbcDataAccessor extends AbstractDataAccessor {
	
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> fetch(QueryInfo queryInfo) {
		jdbcTemplate.setFetchSize(getFetchSize());
		
		Object[] args = null;
		if (queryInfo.getParameters() != null && !queryInfo.getParameters().isEmpty()) {
			args = queryInfo.getParameters().toArray();
		}
		
		return jdbcTemplate.queryForList(queryInfo.getSql(), args);
	}

}
