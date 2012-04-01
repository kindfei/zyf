package fei.tools.dc.da;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public abstract class HibernateDataAccessor extends AbstractDataAccessor {
	
	private HibernateTemplate hibernateTemplate;
	
	private int fetchStep;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public int getFetchStep() {
		return fetchStep;
	}

	public void setFetchStep(int fetchStep) {
		this.fetchStep = fetchStep;
	}
	
	protected abstract Query createQuery(Session session, String sql);
	protected abstract List<Map<String, Object>> convert(List<?> results);

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> fetch(final QueryInfo queryInfo) {
		return (List<Map<String, Object>>) hibernateTemplate.execute(new HibernateCallback() {
			public List<Map<String, Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				
				//
				String sql = queryInfo.getSql();
				if (sql == null || sql.isEmpty()) {
					throw new IllegalArgumentException("queryInfo missing required property 'sql'");
				}
				
				//
				Query query = createQuery(session, sql);
				
				List<Object> parameters = queryInfo.getParameters();
				if (parameters != null && !parameters.isEmpty()) {
					Iterator<Object> iter = parameters.iterator();
					for (int i = 0; iter.hasNext(); i++) {
						query.setParameter(i, iter.next());
					}
				}
				
				if (getFetchSize() > 0) {
					query.setFetchSize(getFetchSize());
				}
				
				if (fetchStep > 0) {
					return convert(fetchByStep(query, fetchStep));
				}
				
				return convert(query.list());
			}
		});
	}
	
	private List<Object> fetchByStep(Query query, int step) {
		List<Object> r = new ArrayList<Object>();
		
		query.setMaxResults(step);
		
		boolean hasMore = true;
		for (int i = 0; hasMore; i++) {
			query.setFirstResult(step * i);
			
			List<?> l = query.list();
			
			if (!l.isEmpty()) r.addAll(l);
			
			if (l.size() < step) hasMore = false;
		}
		
		return r;
	}
}
