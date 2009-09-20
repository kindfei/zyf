package jp.emcom.adv.n225.core.start;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Classpath {

	private List<File> elements = new ArrayList<File>();

	public Classpath() {
	}

	public Classpath(String initial) {
		addClasspath(initial);
	}

	public boolean addComponent(String component) {
		if ((component != null) && (component.length() > 0)) {
			return addComponent(new File(component));
		}
		return false;
	}

	public boolean addComponent(File component) {
		if (component != null) {
			try {
				if (component.exists()) {
					File key = component.getCanonicalFile();
					if (!elements.contains(key)) {
						elements.add(key);
						return true;
					}
				}
			} catch (IOException e) {
			}
		}
		return false;
	}

	public boolean addClasspath(String s) {
		boolean added = false;
		if (s != null) {
			StringTokenizer t = new StringTokenizer(s, File.pathSeparator);
			while (t.hasMoreTokens()) {
				added |= addComponent(t.nextToken());
			}
		}
		return added;
	}

	private void appendPath(StringBuilder cp, String path) {
		if (path.indexOf(' ') >= 0) {
			cp.append('\"');
			cp.append(path);
			cp.append('"');
		} else {
			cp.append(path);
		}
	}

	public String toString() {
		StringBuilder cp = new StringBuilder(1024);
		int cnt = elements.size();
		if (cnt >= 1) {
			cp.append(elements.get(0).getPath());
		}
		for (int i = 1; i < cnt; i++) {
			cp.append(File.pathSeparatorChar);
			appendPath(cp, elements.get(i).getPath());
		}
		return cp.toString();
	}

	public URL[] getUrls() {
		int cnt = elements.size();
		URL[] urls = new URL[cnt];
		for (int i = 0; i < cnt; i++) {
			try {
				urls[i] = elements.get(i).toURI().toURL();
			} catch (MalformedURLException e) {
				// note: this is printing right to the console because at this
				// point we don't have the rest of the system up, not even the
				// logging stuff
				System.out.println("Error adding classpath entry: " + e.toString());
				e.printStackTrace();
			}
		}
		return urls;
	}

	public ClassLoader getClassLoader() {
		URL[] urls = getUrls();

		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = Classpath.class.getClassLoader();
		}
		if (parent == null) {
			parent = ClassLoader.getSystemClassLoader();
		}
		return new URLClassLoader(urls, parent);
	}

	public List<File> getElements() {
		return elements;
	}
}
