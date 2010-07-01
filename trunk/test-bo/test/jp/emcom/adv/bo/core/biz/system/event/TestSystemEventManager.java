package jp.emcom.adv.bo.core.biz.system.event;

import jp.emcom.adv.bo.core.utils.CommandUtils;
import jp.emcom.adv.bo.test.ContextLoader;

/**
 * @author zhangyf
 *
 */
public class TestSystemEventManager extends ContextLoader {
	public static void main(String[] args) throws Exception {
		TestSystemEventManager inst = new TestSystemEventManager();
		
		SystemEventManager manager = (SystemEventManager) inst.getBean("remoteSystemEventManager");

		String[] opts = new String[] {
				"add listener",
				"remove listener",
				"send Event",
				"get listeners"
		};
		
		for (;;) {
			int i = CommandUtils.choose(opts);
			
			switch (i) {
			case 0:
				manager.addEventListener(new SystemEventListener() {
					@Override
					public void onEvent(SystemEvent event) {
						System.out.println(this.hashCode() + " Received Message: " + event);
						}
					}
				);
				break;
			case 1:
				System.out.println(manager.removeEventListener(manager.getEventListeners().get(0)));
				break;
			case 2:
				manager.publish(new SystemEvent(SystemEventType.KICK_OUT_CUSTOMER));
				break;
			case 3:
				System.out.println(manager.getEventListeners().size());
				break;
			default:
				break;
			}
		}
	}
}
