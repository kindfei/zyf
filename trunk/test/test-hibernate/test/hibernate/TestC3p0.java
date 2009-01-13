package test.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import core.helper.CmdHelper;
import core.hibernate.SessionUtils;

public class TestC3p0 {
	public static void main(String[] args) {
		try {
			String[] opts = {
					"create SessionHolder",
					"notify SessionHolder",
					"notifyAll SessionHolder",
					"Quit"};
			
			int counter = 0;
			
			a:while (true) {
				int opt = CmdHelper.options(opts);
				switch (opt) {
				case 0:
					int size = Integer.parseInt(CmdHelper.input("size"));
					for (int i = 0; i < size; i++) {
						new SessionHolder("SessionHolder" + counter++).start();
					}
					break;
				case 1:
					int count = Integer.parseInt(CmdHelper.input("count"));
					synchronized (TestC3p0.class) {
						for (int i = 0; i < count; i++) {
							TestC3p0.class.notify();
						}
					}
					break;
				case 2:
					synchronized (TestC3p0.class) {
						TestC3p0.class.notifyAll();
					}
					break;
				default:
					break a;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static class SessionHolder extends Thread {
		
		public SessionHolder(String name) {
			super(name);
		}
		
		@Override
		public void run() {
			Session session = SessionUtils.MySQL.getSession();
			
			Transaction tx = session.getTransaction();
			
			tx.begin();
			
			System.out.println(getName() + "-successfully begin the transaction.");
			
			synchronized (TestC3p0.class) {
				try {
					TestC3p0.class.wait();
				} catch (InterruptedException e) {
				}
			}
			
			tx.commit();
			
			SessionUtils.MySQL.closeSession();
			
			System.out.println(getName() + "-session is closed.");
		}
	}
}
