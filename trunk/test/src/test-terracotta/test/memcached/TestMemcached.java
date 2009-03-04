package test.memcached;

import java.util.Calendar;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class TestMemcached {
	private SockIOPool pool;
	private MemCachedClient mc;
	
	public TestMemcached() {
		String[] serverlist = { "localhost:11211" };
		
		pool = SockIOPool.getInstance( "test" );
		pool.setServers(serverlist);

		pool.setInitConn( 100 );
		pool.setMinConn( 100 );
		pool.setMaxConn( 500 );
		pool.setMaintSleep( 20 );

		pool.setNagle( false );
		pool.initialize();

		// get client instance
		mc = new MemCachedClient( "test" );
		mc.setCompressEnable( false );
	}
	
	private void put() {
		long st = Calendar.getInstance().getTimeInMillis();
		mc.set("st", st);
	}
	
	private void take() {
		Object obj = mc.get("st");
		if (obj == null) {
			return;
		}
		long st = (Long) obj;
		long elapse = Calendar.getInstance().getTimeInMillis() - st;
		System.out.println("elapse = " + elapse + "ms");
		mc.delete("st");
	}
	
	public void testSet() {
		try {
			for (;;) {
				put();
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGet() {
		try {
			for (;;) {
				take();
				Thread.sleep(1);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TestMemcached inst = new TestMemcached();
		inst.testGet();
	}
}
