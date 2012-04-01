package fei.tools.dc.da;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

public class NativeDataAccessor extends HibernateDataAccessor {

	@Override
	protected Query createQuery(Session session, String sql) {
		return session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Map<String, Object>> convert(List<?> results) {
		return (List<Map<String, Object>>) results;
	}

}
