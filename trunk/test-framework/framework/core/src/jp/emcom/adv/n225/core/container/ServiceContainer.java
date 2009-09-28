package jp.emcom.adv.n225.core.container;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.emcom.adv.n225.core.component.ComponentConfig;
import jp.emcom.adv.n225.core.component.ComponentResourceHandler;
import jp.emcom.adv.n225.core.component.ComponentConfig.ServiceResourceInfo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ServiceContainer implements Container {
	Map<String, ApplicationContext> service = new HashMap<String, ApplicationContext>();

	public void init(String configFile) throws Exception {
		Map<String, List<ServiceResourceInfo>> infosMap = ComponentConfig.getAllServiceResourceInfosByName("spring");
		for (Map.Entry<String, List<ServiceResourceInfo>> entry : infosMap.entrySet()) {
			String name = entry.getKey();
			List<ServiceResourceInfo> infos = entry.getValue();
			String[] files = new String[infos.size()];
			Iterator<ServiceResourceInfo> iterator = infos.iterator();
			for (int i = 0; iterator.hasNext(); i++) {
				ComponentResourceHandler handler = iterator.next().createResourceHandler();
				String file = handler.getFullLocation();
				files[i] = file;
			}
			service.put(name, new FileSystemXmlApplicationContext(files));
		}
	}

	public boolean start() throws Exception {
		return true;
	}

	public void stop() throws Exception {

	}

}
