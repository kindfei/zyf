package test.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import test.db.po.Test;
import test.db.po.TestAliasBean;
import test.util.Utils;

public class TestQuery {
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

	/**
	 * 
	 */
	public void testJDBC() {
		jdbcTemplate.execute("select * from test where id = ?", 
			new PreparedStatementCallback() {
				@Override
				public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					ps.setInt(1, 1);
					
					ResultSet rs = ps.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					while (rs.next()) {
						for (int i = 1; i < columnCount + 1; i++) {
							String key = rsmd.getColumnLabel(i);
							if (key == null || key.isEmpty()) {
								key = rsmd.getColumnName(i);
							}
							System.out.println(key + " - " + rs.getObject(i));
						}
					}
					
					return null;
				}
			}
		);
	}
	
	public void testJDBCConn() {
		jdbcTemplate.execute(new ConnectionCallback() {
			@Override
			public Object doInConnection(Connection con) throws SQLException, DataAccessException {
				con.getAutoCommit();
				return null;
			}
		});
	}
	
	/**
	 * 
	 */
	public void testHibernate() {
		hibernateTemplate.executeWithNewSession(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Object r = session.get(Test.class, 1);
				System.out.println(r);
				return null;
			}
		});
	}
	
	/**
	 * 
	 */
	public void testHibernateSQL() {
		hibernateTemplate.executeWithNewSession(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery q = session.createSQLQuery("select * from test where id = ?");
				q.setResultTransformer(new AliasToBeanResultTransformer(TestAliasBean.class));
				q.setInteger(0, 1);
				System.out.println(q.uniqueResult());
				return null;
			}
		});
	}
	
	public void testHibernateList() {
		hibernateTemplate.executeWithNewSession(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<?> r = null;
				
				r = session.createQuery("select id, version from Test where id = :id")
				.setInteger("id", 0)
				.list();
				
				System.out.println(toStr(r));

				r = session.createQuery("select id from Test where id = :id")
				.setInteger("id", 0)
				.list();
				
				System.out.println(toStr(r));

				r = session.createSQLQuery("select id, version from test where id = :id")
				.setInteger("id", 0)
				.list();
				
				System.out.println(toStr(r));

				r = session.createSQLQuery("select id from test where id = :id")
				.setInteger("id", 0)
				.list();
				
				System.out.println(toStr(r));
				
				return null;
			}
		});
	}
	
	private String toStr(Object obj) {
		String r = "";
		if (obj instanceof Collection<?>) {
			r += "C[";
			for (Iterator<?> i = ((Collection<?>) obj).iterator(); i.hasNext();) {
				Object o = i.next();
				r += toStr(o);
				if (i.hasNext()) r += ",";
			}
			r += "]";
		} else if (obj.getClass().isArray()) {
			r += "A[";
			Object[] objs = (Object[]) obj;
			for (int j = 0; j < objs.length; j++) {
				if (j > 0) r += ",";
				r += toStr(objs[j]);
			}
			r += "]";
		} else {
			r += obj.toString();
		}
		return r;
	}
	
	void test1() {
		hibernateTemplate.executeWithNewSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Object[] r = (Object[]) session.createSQLQuery("select * from TblSodPosition where subAcct='5229487T' and cusip='00817Y108000'")
				.uniqueResult();
				System.out.println(toStr(r));
				
				BigDecimal mnp = (BigDecimal) r[4];
				BigDecimal snp = (BigDecimal) r[5];
				Date ut = (Date) r[6];
				
				System.out.println(mnp);
				System.out.println(snp);
				System.out.println(ut);
				
				return null;
			}
		});
	}

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext c = Utils.loadContext(TestQuery.class, "context.xml");
		TestQuery inst = (TestQuery) c.getBean("testQuery");

		inst.test1();
	}
}
