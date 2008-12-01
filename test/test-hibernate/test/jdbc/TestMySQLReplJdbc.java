package test.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;


import com.mysql.jdbc.Driver;
import com.mysql.jdbc.ReplicationDriver;

import core.helper.CmdHelper;

public class TestMySQLReplJdbc {
	private Connection conn = null;
	
	public TestMySQLReplJdbc() {
	}
	
	void normConnect() {
		try {
			Driver driver = new Driver();
			
			Properties props = new Properties();
			props.setProperty("user", "root");
			props.setProperty("password", "123456");

			props.setProperty("useUnicode", "true");
			props.setProperty("characterEncoding", "ms932");
			
			conn = driver.connect("jdbc:mysql://10.4.6.221,10.4.6.228,10.4.6.229/test", props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void replConnect() {
		try {
			ReplicationDriver driver = new ReplicationDriver();
			
			Properties props = new Properties();
			props.setProperty("autoReconnect", "true");
			props.setProperty("roundRobinLoadBalance", "true");
			props.setProperty("user", "root");
			props.setProperty("password", "123456");

			props.setProperty("useUnicode", "true");
			props.setProperty("failOverReadOnly", "false");
			props.setProperty("characterEncoding", "ms932");
			
			conn = driver.connect("jdbc:mysql://10.4.6.221,10.4.6.228,10.4.6.229/test", props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void dmConnect(boolean readOnly) {
		try {
			Class.forName("com.mysql.jdbc.ReplicationDriver");
			
			DriverManager.setLoginTimeout(1);
			java.sql.Driver driver = DriverManager.getDriver("jdbc:mysql:replication://10.4.6.221,10.4.6.228,10.4.6.229/test");
			
			Properties props = new Properties();
			props.setProperty("user", "root");
			props.setProperty("password", "123456");

			props.setProperty("useUnicode", "true");
			props.setProperty("characterEncoding", "ms932");
			
			conn = driver.connect("jdbc:mysql:replication://10.4.6.221,10.4.6.228,10.4.6.229/test", props);
			conn.setReadOnly(readOnly);
			System.out.println("Connected. readOnly=" + readOnly);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showInfos() throws SQLException {
		MetaDataUtil.printInfos(conn);
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int counter;
	
	public void insertDatas(int size) throws SQLException {
		for (int i = 0; i < size; i++, counter++) {
			String id = "test" + counter;
			insert(id);
			System.out.println(id + " was inserted.");
		}
	}
	
	public void insert(String id) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement("insert into zyf_test (test_id, test_name) values (?, 'test.jdbc')");
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		pstmt.close();
	}
	
	public static void main(String[] args) {
		TestMySQLReplJdbc inst = new TestMySQLReplJdbc();
		
		String[] opts = {
			"normConnect",
			"replConnect",
			"dmConnect",
			"print info",
			"insert data",
			"exit"
		};
		
		try {
			DriverManager.setLogWriter(new PrintWriter(System.out));
//			DriverManager.registerDriver(new ReplicationDriver());
			a:for (;;) {
				int opt = CmdHelper.options(opts);
				switch (opt) {
				case 0:
					inst.normConnect();
					break;
				case 1:
					inst.replConnect();
					break;
				case 2:
					inst.dmConnect("true".equalsIgnoreCase(CmdHelper.input("readOnly")) ? true : false);
					break;
				case 3:
					inst.showInfos();
					break;
				case 4:
					inst.insertDatas(Integer.parseInt(CmdHelper.input("Count of data:")));
					break;
				case 5:
					System.out.println("Closing connection.");
					break a;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		inst.close();
	}
}
