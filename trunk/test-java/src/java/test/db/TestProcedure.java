package test.db;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import test.db.po.TestAliasBean;
import test.util.Utils;


public class TestProcedure {
	
	private JdbcTemplate jdbcTemplate;
	private HibernateTemplate hibernateTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	private List<List<Map<String, Object>>> execute(CallableStatement cs) throws SQLException {
		List<List<Map<String, Object>>> r = new ArrayList<List<Map<String,Object>>>();
		
		boolean retval = cs.execute();
		ResultSet rs;
		int count;
		do {
			if (retval == false) {
				count = cs.getUpdateCount();
				if (count == -1) {
					break; // no more results
				} else {
					// process update count
					System.out.println("updateCount: " + count);
				}
			} else { // ResultSet
				rs = cs.getResultSet();
				
				// process ResultSet
				System.out.println("resultSet: " + rs);
				List<Map<String, Object>> e = new ArrayList<Map<String,Object>>();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					for (int i = 1; i < columnCount + 1; i++) {
						String key = rsmd.getColumnLabel(i);
						if (key == null || key.isEmpty()) {
							key = rsmd.getColumnName(i);
						}
						row.put(key, rs.getObject(i));
						System.out.println(key + " - " + rs.getObject(i));
					}
					e.add(row);
				}
				r.add(e);
			}
			retval = cs.getMoreResults();
		} while (true);
		
		return r;
	}

	/**
	 * 
	 * @return
	 */
	public Object testJdbc() {
		return jdbcTemplate.execute("{? = call testInsTest(?, ?, ?)}", new CallableStatementCallback() {
			@Override
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.registerOutParameter(1, java.sql.Types.TINYINT);
				cs.registerOutParameter(2, java.sql.Types.TINYINT);
				cs.registerOutParameter(3, java.sql.Types.TINYINT);
				
				cs.setNull(2, Types.INTEGER);
				cs.setNull(3, Types.INTEGER);
				cs.setString(4, "testProcedure");
				
				execute(cs);
				
				System.out.println(cs.getInt(1));
				System.out.println(cs.getInt(2));
				System.out.println(cs.getInt(3));
				
				ResultSet rs = cs.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				while (rs.next()) {
					for (int i = 1; i < columnCount + 1; i++) {
						String key = rsmd.getColumnLabel(i);
						if (key == null || key.isEmpty()) {
							key = rsmd.getColumnName(i);
						}
						System.out.println(key + " - " + rs.getObject(i) + " - " + rsmd.getColumnTypeName(i));
					}
				}
				
				return null;
			}
		});
	}

	/**
	 * 
	 * @param toMap
	 * @return
	 */
	public Object testHibernate(final boolean toMap) {
		return hibernateTemplate.executeWithNewSession(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery("{? = call testInsTest(?, ?, ?, ?)}");
				
				if (toMap) {
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				} else {
					query.addScalar("id", Hibernate.INTEGER);
					query.addScalar("version", Hibernate.INTEGER);
					query.addScalar("content", Hibernate.STRING);
					query.addScalar("date_time", Hibernate.TIMESTAMP);
					query.setResultTransformer(new AliasToBeanResultTransformer(TestAliasBean.class));
				}

				query.setParameter(0, null);
				query.setParameter(1, null);
				query.setParameter(2, "testProcedure");
				query.setParameter(3, null);
				
				List<?> r = query.list();
				for (Object o : r) {
					if (o.getClass().isArray()) {
						System.out.println(Arrays.toString((Object[]) o));
					} else {
						System.out.println(o.toString());
					}
				}
				
				return null;
			}
		});
	}
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext c = Utils.loadContext(TestProcedure.class, "context.xml");
		TestProcedure testProcedure = (TestProcedure) c.getBean("testProcedure");
		
		testProcedure.testJdbc();
	}
	
}
