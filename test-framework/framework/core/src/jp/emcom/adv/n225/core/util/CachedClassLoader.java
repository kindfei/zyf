package jp.emcom.adv.n225.core.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.spi.ServiceRegistry;

import org.apache.log4j.Logger;

/**
 * 
 * @author alex
 *
 */
public class CachedClassLoader extends URLClassLoader {
	private static Logger log;//TODO

    public interface Init {
        void loadClasses(ClassLoader loader, Map<String, Class<?>> classNameMap) throws ClassNotFoundException;
    }

    private String contextName;

    public static Map<String, Class<?>> globalClassNameClassMap = new HashMap<String, Class<?>>();
    public static HashSet<String> globalBadClassNameSet = new HashSet<String>();

    public Map<String, Class<?>> localClassNameClassMap = new HashMap<String, Class<?>>();
    public HashSet<String> localBadClassNameSet = new HashSet<String>();

    public static Map<String, URL> globalResourceMap = new HashMap<String, URL>();
    public static HashSet<String> globalBadResourceNameSet = new HashSet<String>();

    public Map<String, URL> localResourceMap = new HashMap<String, URL>();
    public HashSet<String> localBadResourceNameSet = new HashSet<String>();

    static {
        // setup some commonly used classes...
        globalClassNameClassMap.put("Object", java.lang.Object.class);
        globalClassNameClassMap.put("java.lang.Object", java.lang.Object.class);

        globalClassNameClassMap.put("String", java.lang.String.class);
        globalClassNameClassMap.put("java.lang.String", java.lang.String.class);

        globalClassNameClassMap.put("Boolean", java.lang.Boolean.class);
        globalClassNameClassMap.put("java.lang.Boolean", java.lang.Boolean.class);

        globalClassNameClassMap.put("BigDecimal", java.math.BigDecimal.class);
        globalClassNameClassMap.put("java.math.BigDecimal", java.math.BigDecimal.class);
        globalClassNameClassMap.put("Double", java.lang.Double.class);
        globalClassNameClassMap.put("java.lang.Double", java.lang.Double.class);
        globalClassNameClassMap.put("Float", java.lang.Float.class);
        globalClassNameClassMap.put("java.lang.Float", java.lang.Float.class);
        globalClassNameClassMap.put("Long", java.lang.Long.class);
        globalClassNameClassMap.put("java.lang.Long", java.lang.Long.class);
        globalClassNameClassMap.put("Integer", java.lang.Integer.class);
        globalClassNameClassMap.put("java.lang.Integer", java.lang.Integer.class);
        globalClassNameClassMap.put("Short", java.lang.Short.class);
        globalClassNameClassMap.put("java.lang.Short", java.lang.Short.class);

        globalClassNameClassMap.put("Byte", java.lang.Byte.class);
        globalClassNameClassMap.put("java.lang.Byte", java.lang.Byte.class);
        globalClassNameClassMap.put("Character", java.lang.Character.class);
        globalClassNameClassMap.put("java.lang.Character", java.lang.Character.class);

        globalClassNameClassMap.put("Timestamp", java.sql.Timestamp.class);
        globalClassNameClassMap.put("java.sql.Timestamp", java.sql.Timestamp.class);
        globalClassNameClassMap.put("Time", java.sql.Time.class);
        globalClassNameClassMap.put("java.sql.Time", java.sql.Time.class);
        globalClassNameClassMap.put("Date", java.sql.Date.class);
        globalClassNameClassMap.put("java.sql.Date", java.sql.Date.class);

        globalClassNameClassMap.put("Locale", java.util.Locale.class);
        globalClassNameClassMap.put("java.util.Locale", java.util.Locale.class);

        globalClassNameClassMap.put("java.util.Date", java.util.Date.class);
        globalClassNameClassMap.put("Collection", java.util.Collection.class);
        globalClassNameClassMap.put("java.util.Collection", java.util.Collection.class);
        globalClassNameClassMap.put("List", java.util.List.class);
        globalClassNameClassMap.put("java.util.List", java.util.List.class);
        globalClassNameClassMap.put("Set", java.util.Set.class);
        globalClassNameClassMap.put("java.util.Set", java.util.Set.class);
        globalClassNameClassMap.put("Map", java.util.Map.class);
        globalClassNameClassMap.put("java.util.Map", java.util.Map.class);
        globalClassNameClassMap.put("HashMap", java.util.HashMap.class);
        globalClassNameClassMap.put("java.util.HashMap", java.util.HashMap.class);

        // setup the primitive types
        globalClassNameClassMap.put("boolean", Boolean.TYPE);
        globalClassNameClassMap.put("short", Short.TYPE);
        globalClassNameClassMap.put("int", Integer.TYPE);
        globalClassNameClassMap.put("long", Long.TYPE);
        globalClassNameClassMap.put("float", Float.TYPE);
        globalClassNameClassMap.put("double", Double.TYPE);
        globalClassNameClassMap.put("byte", Byte.TYPE);
        globalClassNameClassMap.put("char", Character.TYPE);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Iterator<Init> cachedClassLoaders = ServiceRegistry.lookupProviders(Init.class, loader);
        while (cachedClassLoaders.hasNext()) {
            Init cachedClassLoader = cachedClassLoaders.next();
            try {
                cachedClassLoader.loadClasses(loader, globalClassNameClassMap);
            } catch (ClassNotFoundException e) {
                log.error("Could not pre-initialize dynamically loaded class: ", e);
            }
        }
    }

    public CachedClassLoader(URL[] url, ClassLoader parent, String contextName) {
        super(url, parent);
        this.contextName = contextName;
    }

    public CachedClassLoader(ClassLoader parent, String contextName) {
        this(new URL[0], parent, contextName);
    }

    public CachedClassLoader(URL[] url, ClassLoader parent) {
        this(url, parent, "__globalContext");
    }

    public String toString() {
        return this.getClass().getName() + "(" + contextName + ") / " + getParent().toString();
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //check glocal common classes, ie for all instances
        Class<?> theClass = globalClassNameClassMap.get(name);

        //check local classes, ie for this instance
        if (theClass == null) theClass = localClassNameClassMap.get(name);

        //make sure it is not a known bad class name
        if (theClass == null) {
            if (localBadClassNameSet.contains(name) || globalBadClassNameSet.contains(name)) {
                throw new ClassNotFoundException("Cached loader got a known bad class name: " + name);
            }
        }

        if (theClass == null) {

            synchronized (this) {
                theClass = localClassNameClassMap.get(name);
                if (theClass == null) {
                    try {
                        theClass = super.loadClass(name, resolve);
                        if (isGlobalPath(name)) {
                            globalClassNameClassMap.put(name, theClass);
                        } else {
                            localClassNameClassMap.put(name, theClass);
                        }
                    } catch (ClassNotFoundException e) {
                        if (isGlobalPath(name)) {
                            globalBadClassNameSet.add(name);
                        } else {
                            localBadClassNameSet.add(name);
                        }
                        throw e;
                    }
                }
            }
        }
        return theClass;
    }

    public URL getResource(String name) {
        //check glocal common resources, ie for all instances
        URL theResource = globalResourceMap.get(name);

        //check local resources, ie for this instance
        if (theResource == null) theResource = localResourceMap.get(name);

        //make sure it is not a known bad resource name
        if (theResource == null) {
            if (localBadResourceNameSet.contains(name) || globalBadResourceNameSet.contains(name)) {
                return null;
            }
        }

        if (theResource == null) {

            synchronized (this) {
                theResource = localResourceMap.get(name);
                if (theResource == null) {
                    theResource = super.getResource(name);
                    if (theResource == null) {
                        if (isGlobalPath(name)) {
                            globalBadResourceNameSet.add(name);
                        } else {
                            localBadResourceNameSet.add(name);
                        }
                    } else {
                        if (isGlobalPath(name)) {
                            globalResourceMap.put(name, theResource);
                        } else {
                            localResourceMap.put(name, theResource);
                        }
                    }
                }
            }
        }
        return theResource;
    }

    protected boolean isGlobalPath(String name) {
        if (name.startsWith("java.") || name.startsWith("java/") || name.startsWith("/java/")) return true;
        if (name.startsWith("javax.") || name.startsWith("javax/") || name.startsWith("/javax/")) return true;
        if (name.startsWith("sun.") || name.startsWith("sun/") || name.startsWith("/sun/")) return true;
        return false;
    }
}
