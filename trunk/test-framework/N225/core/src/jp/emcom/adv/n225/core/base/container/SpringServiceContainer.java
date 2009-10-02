package jp.emcom.adv.n225.core.base.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.emcom.adv.n225.core.base.component.ComponentConfig;
import jp.emcom.adv.n225.core.base.component.ComponentResourceHandler;
import jp.emcom.adv.n225.core.base.component.ComponentConfig.ServiceResourceInfo;
import jp.emcom.adv.n225.test.service.TestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringServiceContainer implements Container {
	private static final Logger log = LoggerFactory.getLogger(SpringServiceContainer.class);

	Map<String, AbstractApplicationContext> parentServices = new HashMap<String, AbstractApplicationContext>();
	Map<String, AbstractApplicationContext> services = new HashMap<String, AbstractApplicationContext>();

	public void init(String configFile) throws Exception {

		initParents();

		Map<String, List<ServiceResourceInfo>> infosMap = ComponentConfig.getAllServiceResourceInfosByName("spring");
		for (Map.Entry<String, List<ServiceResourceInfo>> entry : infosMap.entrySet()) {
			String name = entry.getKey();

			List<ServiceResourceInfo> infos = entry.getValue();

			String[] files = new String[infos.size()];

			AbstractApplicationContext parent = null;

			Iterator<ServiceResourceInfo> iterator = infos.iterator();
			for (int i = 0; iterator.hasNext(); i++) {
				ServiceResourceInfo info = iterator.next();
				if (info.parent != null && info.parent.length() > 0) {
					if (parent == null) {
						parent = parentServices.get(info.parent);
					} else {
						throw new Exception("Duplicate parent ApplicationContext error. componentName=" + name);
					}
				}
				ComponentResourceHandler handler = info.createResourceHandler();
				String file = handler.getFullLocation();
				files[i] = file;
			}

			if (parent != null) {
				services.put(name, new FileSystemXmlApplicationContext(files, parent));
			} else {
				services.put(name, new FileSystemXmlApplicationContext(files));
			}
		}
	}

	private void initParents() throws Exception {
		List<ServiceResourceInfo> infos = ComponentConfig.getAllServiceResourceInfos("spring-parent");

		List<ServiceResourceInfo> infosInOrder = new ArrayList<ServiceResourceInfo>(infos.size());

		classify(null, infos, infosInOrder);

		if (infos.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (ServiceResourceInfo info : infos) {
				sb.append(" [id=").append(info.id).append(" parent=").append(info.parent).append("]");
			}
			log.warn("There is illegal spring-parent element.{}", sb.toString());
		}

		for (ServiceResourceInfo info : infosInOrder) {
			ComponentResourceHandler handler = info.createResourceHandler();
			String file = handler.getFullLocation();

			ApplicationContext parent = null;
			if (info.parent != null && info.parent.length() > 0) {
				parent = parentServices.get(info.parent);
			}

			if (parent != null) {
				parentServices.put(info.id, new FileSystemXmlApplicationContext(new String[] { file }, parent));
			} else {
				parentServices.put(info.id, new FileSystemXmlApplicationContext(file));
			}
		}
	}

	private void classify(List<ServiceResourceInfo> parents, List<ServiceResourceInfo> source,
			List<ServiceResourceInfo> target) {

		if (parents == null) {
			parents = new ArrayList<ServiceResourceInfo>();
			parents.add(null);
		}

		List<ServiceResourceInfo> children = new ArrayList<ServiceResourceInfo>();

		for (ServiceResourceInfo parent : parents) {
			for (Iterator<ServiceResourceInfo> iterator = source.iterator(); iterator.hasNext();) {
				ServiceResourceInfo info = iterator.next();
				if (parent == null) {
					if (info.parent == null || info.parent.length() == 0) {
						children.add(info);
						iterator.remove();
					}
				} else if (parent.id != null && parent.id.length() > 0) {
					if (info.parent != null && info.parent.length() > 0 && info.parent.equals(parent.id)) {
						children.add(info);
						iterator.remove();
					}
				}
			}
		}

		target.addAll(children);

		if (children.size() > 0) {
			classify(children, source, target);
		}
	}

	public boolean start() throws Exception {

		ApplicationContext childrenContext = services.get("test");
		ApplicationContext parent2Context = parentServices.get("core-parent2");

		TestService children = (TestService) childrenContext.getBean("child1");
		TestService parent1 = (TestService) parent2Context.getBean("parent1");

		System.out.println("child1 parent1 said: " + children.getParent().sayHello());
		System.out.println("parent1 said: " + parent1.sayHello());

		children.getParent().setName("kid1");

		System.out.println("hild1 parent1 said: " + children.getParent().sayHello());
		System.out.println("parent1 said: " + parent1.sayHello());

		return true;
	}

	public void stop() throws Exception {
		for (AbstractApplicationContext context : services.values()) {
			context.close();
		}
	}

}
