package test.copy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.beans.BeanCopier;

/**
 * 
 * @author yz69579
 * 
 */
public class CopyUtil {
	private static final ConcurrentHashMap<Class<?>, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<Class<?>, BeanCopier>();

	private static final Set<Class<?>> PRIMITIVES_AND_WRAPPERS = new HashSet<Class<?>>();
	static {
		PRIMITIVES_AND_WRAPPERS.add(boolean.class);
		PRIMITIVES_AND_WRAPPERS.add(Boolean.class);
		PRIMITIVES_AND_WRAPPERS.add(byte.class);
		PRIMITIVES_AND_WRAPPERS.add(Byte.class);
		PRIMITIVES_AND_WRAPPERS.add(char.class);
		PRIMITIVES_AND_WRAPPERS.add(Character.class);
		PRIMITIVES_AND_WRAPPERS.add(double.class);
		PRIMITIVES_AND_WRAPPERS.add(Double.class);
		PRIMITIVES_AND_WRAPPERS.add(float.class);
		PRIMITIVES_AND_WRAPPERS.add(Float.class);
		PRIMITIVES_AND_WRAPPERS.add(int.class);
		PRIMITIVES_AND_WRAPPERS.add(Integer.class);
		PRIMITIVES_AND_WRAPPERS.add(long.class);
		PRIMITIVES_AND_WRAPPERS.add(Long.class);
		PRIMITIVES_AND_WRAPPERS.add(short.class);
		PRIMITIVES_AND_WRAPPERS.add(Short.class);
	}

	private static final Set<Class<?>> IMMUTABLE_CLASSES = new HashSet<Class<?>>();
	static {
		IMMUTABLE_CLASSES.add(Boolean.class);
		IMMUTABLE_CLASSES.add(Byte.class);
		IMMUTABLE_CLASSES.add(Character.class);
		IMMUTABLE_CLASSES.add(Double.class);
		IMMUTABLE_CLASSES.add(Float.class);
		IMMUTABLE_CLASSES.add(Integer.class);
		IMMUTABLE_CLASSES.add(Long.class);
		IMMUTABLE_CLASSES.add(Short.class);
		IMMUTABLE_CLASSES.add(Class.class);
		IMMUTABLE_CLASSES.add(String.class);
		IMMUTABLE_CLASSES.add(BigDecimal.class);
		IMMUTABLE_CLASSES.add(BigInteger.class);
		IMMUTABLE_CLASSES.add(java.util.Date.class);
		IMMUTABLE_CLASSES.add(java.sql.Date.class);
	}

	public static Object copy(Object obj) {
		if (obj == null) {
			return null;
		}

		final Class<?> clazz = obj.getClass();

		//
		if (PRIMITIVES_AND_WRAPPERS.contains(clazz)) {
			return obj;
		}

		if (IMMUTABLE_CLASSES.contains(clazz)) {
			return obj;
		}

		//
		if (obj.getClass().isArray()) {
			return arrayCopy(obj);
		}

		if (Set.class.isAssignableFrom(clazz)) {
			return setCopy((Set<?>) obj);
		}

		if (Map.class.isAssignableFrom(clazz)) {
			return mapCopy((Map<?, ?>) obj);
		}

		if (List.class.isAssignableFrom(clazz)) {
			return listCopy((List<?>) obj);
		}

		//
		if (Cloneable.class.isAssignableFrom(clazz)) {
			return cloneCopy(obj);
		}

		if (Serializable.class.isAssignableFrom(clazz)) {
			return serializableCopy((Serializable) obj);
		}

		//
		try {
			Object r = clazz.newInstance();
			copy(obj, r);
			return r;
		} catch (Exception e) {
			throw new RuntimeException("failed to copy " + obj, e);
		}
	}

	private static Object arrayCopy(Object array) {
		final int length = Array.getLength(array);
		final Object r = Array.newInstance(array.getClass().getComponentType(), length);
		for (int i = 0; i < length; i++) {
			final Object element = Array.get(array, i);
			if (element.getClass().isArray()) {
				Array.set(r, i, arrayCopy(element));
			} else {
				Array.set(r, i, copy(element));
			}
		}
		return r;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object setCopy(Set set) {
		try {
			Set r = set.getClass().newInstance();
			for (Object obj : set) {
				r.add(copy(obj));
			}
			return r;
		} catch (Exception e) {
			throw new RuntimeException("failed to copy set", e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object mapCopy(Map map) {
		try {
			Map r = map.getClass().newInstance();
			for (Object key : map.keySet()) {
				r.put(key, copy(map.get(key)));
			}
			return r;
		} catch (Exception e) {
			throw new RuntimeException("failed to copy map", e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object listCopy(List list) {
		try {
			List r = list.getClass().newInstance();
			for (Object obj : list) {
				r.add(copy(obj));
			}
			return r;
		} catch (Exception e) {
			throw new RuntimeException("failed to copy list", e);
		}
	}

	private static Object cloneCopy(Object obj) {
		try {
			final Class<?> clazz = obj.getClass();
			Method method = clazz.getMethod("clone", new Class<?>[] {});
			method.setAccessible(true);
			return method.invoke(obj, new Object[] {});
		} catch (Exception e) {
			throw new RuntimeException("failed to clone copy", e);
		}
	}

	private static Serializable serializableCopy(Serializable obj) {
		return deserialize(serialize(obj));
	}

	private static byte[] serialize(Serializable s) {
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(s);
			oos.flush();
			return bos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("failed to serialize s: " + s, e);
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
			}
		}
	}

	private static Serializable deserialize(byte data[]) {
		return deserialize(data, 0, data.length);
	}

	private static Serializable deserialize(byte data[], int offset, int length) {
		ByteArrayInputStream bis = null;
		try {
			bis = new ByteArrayInputStream(data, offset, length);
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (Serializable) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException("failed to deserialize", e);
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
			}
		}
	}

	private static void copy(Object src, Object dst) {
		final Class<?> clazz = src.getClass();
		BeanCopier copier = BEAN_COPIERS.get(clazz);
		if (copier == null) {
			copier = BeanCopier.create(clazz, clazz, false);
			BeanCopier existing = BEAN_COPIERS.putIfAbsent(clazz, copier);
			if (existing != null) {
				copier = existing;
			}
		}

		copier.copy(src, dst, null);
	}

}
