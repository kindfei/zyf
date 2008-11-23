package test.hibernate;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.mysql.jdbc.ReplicationDriver;

import test.core.hibernate.SessionUtils;
import test.hibernate.tables.mysql.repl.ZyfTest;
import test.jdbc.MetaDataUtil;

import zyf.helper.CmdHelper;

public class TestMySQLReplHibe {
	
	TestMySQLReplHibe() {
	}
	
	private Session getSession() {
		return SessionUtils.getSessionMySQLRepl();
	}
	
	public void showInfo() throws SQLException {
		Session session = getSession();
		Connection conn = session.connection();
		MetaDataUtil.printInfos(conn);
	}
	
	void setReadOnly(boolean readOnly) throws HibernateException, SQLException {
		Session session = getSession();
		session.connection().setReadOnly(readOnly);
		System.out.println("Set readOnly=" + readOnly);
	}
	
	private int counter;
	
	void insert(String id) {
		Session session = getSession();
		ZyfTest bean = new ZyfTest();
		bean.setTestId(id);
		bean.setTestName("test.hibernate");
		
		session.save(bean);
		session.flush();
	}
	
	void commit() {
		Session session = getSession();
		session.beginTransaction().commit();
		System.out.println("Transaction commited");
	}
	
	void insertDatas(int size) {
		for (int i = 0; i < size; i++, counter++) {
			String id = "test" + counter;
			insert(id);
			System.out.println(id + " was inserted.");
		}
	}
	
	void insertDatasWithCommit(int size) {
		for (int i = 0; i < size; i++, counter++) {
			String id = "test" + counter;
			insert(id);
			commit();
			System.out.println(id + " was inserted.");
		}
	}
	
	public static void main(String[] args) {
		TestMySQLReplHibe inst = new TestMySQLReplHibe();
		
		String[] opts = {
			"print info",
			"set readOnly",
			"insert datas",
			"commit transaction",
			"insert datas with commit",
			"exit"
		};
		
		try {
			DriverManager.setLogWriter(new PrintWriter(System.out));
			DriverManager.registerDriver(new ReplicationDriver());
			a:for (;;) {
				int opt = CmdHelper.options(opts);
				switch (opt) {
				case 0:
					inst.showInfo();
					break;
				case 1:
					inst.setReadOnly("true".equalsIgnoreCase(CmdHelper.input("readOnly:")) ? true : false);
					break;
				case 2:
					inst.insertDatas(Integer.parseInt(CmdHelper.input("Count of datas:")));
					break;
				case 3:
					inst.commit();
					break;
				case 4:
					inst.insertDatasWithCommit(Integer.parseInt(CmdHelper.input("Count of datas:")));
					break;
				case 5:
					System.out.println("Closing connection.");
					break a;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
