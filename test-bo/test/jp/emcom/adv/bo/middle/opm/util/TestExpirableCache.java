package jp.emcom.adv.bo.middle.opm.util;

import java.util.Random;

public class TestExpirableCache {

	public static void main(String[] args) throws Exception {
		final ExpirableCache inst = new ExpirableCache();
		inst.setValidPeriod(6000);
		inst.setCheckPeriod(800);
//		inst.setVerbose(false);
		inst.start();
		
		final Random rdm = new Random();
		
		class R implements Runnable {
			public void run() {
				for (int i = 0; i < 100000; i++) {
					String s1 = String.valueOf(rdm.nextInt(100));
					boolean r = inst.addIfAbsent(s1);
//					System.out.println("add " + s1 + ", " + r);
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("======" + inst.cacheSize());
			}
		}
		
		for (int i = 0; i < 20; i++) {
			new Thread(new R()).start();
		}
		
	}
}
