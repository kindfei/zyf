package fei.tools.dc.da;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class JdbcDataAccessor extends AbstractDataAccessor {
	
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> fetch(final QueryInfo queryInfo) {
		
		jdbcTemplate.setFetchSize(getFetchSize());
		
		return (List<Map<String, Object>>) jdbcTemplate.execute(queryInfo.getSql(), new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				
				List<Map<String, Object>> r = new ArrayList<Map<String,Object>>();
				
				List<Object> parameters = queryInfo.getParameters();
				if (parameters != null && !parameters.isEmpty()) {
					Iterator<Object> iter = parameters.iterator();
					for (int i = 1; iter.hasNext(); i++) {
						ps.setObject(i, iter.next());
					}
				}
				
				ResultSet rs = ps.executeQuery();
				
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				while (rs.next()) {
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					for (int i = 1; i < columnCount + 1; i++) {
						String key = rsmd.getColumnLabel(i);
						if (key == null || key.isEmpty()) {
							key = rsmd.getColumnName(i);
						}
						map.put(key, rs.getObject(i));
					}
					r.add(map);
				}
				
				rs.close();
				
				return r;
			}
		});
	}

}
