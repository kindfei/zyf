package jp.emcom.adv.n225.core.container;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.emcom.adv.n225.core.component.ComponentConfig;
import jp.emcom.adv.n225.core.component.ComponentResourceHandler;
import jp.emcom.adv.n225.core.component.ComponentConfig.ServiceResourceInfo;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ServiceContainer implements Container {
	Map<String, AbstractApplicationContext> parentServices = new HashMap<String, AbstractApplicationContext>();
	Map<String, AbstractApplicationContext> services = new HashMap<String, AbstractApplicationContext>();

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
			services.put(name, new FileSystemXmlApplicationContext(files));
		}
	}
	
	private void initParents() throws Exception {
		List<ServiceResourceInfo> infos = ComponentConfig.getAllServiceResourceInfos("spring-parent");
		Map<String, ServiceResourceInfo> infosMap = new HashMap<String, ServiceResourceInfo>();
		for (ServiceResourceInfo info : infos) {
			if (info.id == null || info.id.length() == 0) {
				throw new Exception("spring-parent id should be specified");
			}
			if (infosMap.put(info.id, info) != null) {
				throw new Exception("duplicate spring-parent id be specified. id=" + info.id);
			}
		}
	}

	public boolean start() throws Exception {
		return true;
	}

	public void stop() throws Exception {
		for (AbstractApplicationContext context : services.values()) {
			context.close();
		}
	}

}
