package fei.tools.dc.da;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.EntityMode;
import org.hibernate.Query;
import org.hibernate.Session;

public class HsqlDataAccessor extends HibernateDataAccessor {
	@Override
	protected Query createQuery(Session session, String sql) {
		return session.getSession(EntityMode.MAP).createQuery(sql);
	}

	@Override
	protected List<Map<String, Object>> convert(List<?> results) {
		try {
			return array2Map(results);
		} catch (Exception e) {
			throw new RuntimeException("entity2Map error. results=" + results, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> array2Map(List<?> results) {
		List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
		for (Object result : results) {
			if (result.getClass().isArray()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				Object[] entitys = (Object[]) result;
				for (int i = 0; i < entitys.length; i++) {
					merge(map, (Map<String, Object>) entitys[i], i + "");
				}
				r.add(map);
			} else {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				merge(map, (Map<String, Object>) result, null);
				r.add(map);
			}
		}
		return r;
	}
	
	@SuppressWarnings("unchecked")
	private void merge(Map<String, Object> destMap, Map<String, Object> srcMap, String prefix) {
		for (Entry<String, Object> entry : srcMap.entrySet()) {
			if (entry.getValue() instanceof Map) {
				merge(destMap, (Map<String, Object>) entry.getValue(), buildKey(prefix, entry.getKey()));
			} else {
				destMap.put(buildKey(prefix, entry.getKey()), entry.getValue());
			}
		}
	}
	
	private String buildKey(String prefix, String key) {
		if (prefix != null && !prefix.isEmpty()) {
			return prefix + "." + key;
		} else {
			return key;
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> entity2Map(List<?> results) throws Exception {
		List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
		for (Object object : results) {
			if (object.getClass().isArray()) {
				Object[] entitys = (Object[]) object;
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < entitys.length; i++) {
					Set<Entry<String, Object>> entrys = BeanUtils.describe(entitys[i]).entrySet();
					for (Entry<String, Object> entry : entrys) {
						map.put(i + "." + entry.getKey(), entry.getValue());
					}
				}
				r.add(map);
			} else {
				r.add(BeanUtils.describe(object));
			}
		}
		return r;
	}
}
