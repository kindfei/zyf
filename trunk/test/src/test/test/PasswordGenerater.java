package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class PasswordGenerater {
	
	String user = "db2inst1";
	String password = "09YCim/9";
	String url = "jdbc:db2://10.0.154.119:50000/db2afx";
	
	Connection connection; 
	
	char[] character = {'A','B','C','D','E','F','G','H','I','G','K',
			'L','M','N','O','P','Q','R','S','T','U','V','W','X','Y',
			'Z','a','b','c','d','e','f','g','h','i','g','k','l','m',
			'n','o','p','q','r','s','t','u','v','w','x','y','z','1',
			'2','3','4','5','6','7','8','9','0'};
	
	PasswordGenerater() {
		
	}
	
	String genrate(int len) {
		char[] pwd = new char[len];
		Random rdm = new Random();
		for (int i=0; i<len; i++) {
			pwd[i] = character[rdm.nextInt(62)];
		}
		return new String(pwd);
	}
	
	void init() throws ClassNotFoundException, SQLException {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		connection = (Connection) DriverManager.getConnection(url, user, password);
	}
	
	ArrayList selCustomerId() throws SQLException {
		String sql = "select customer_id from customer";
		Statement stat = null;
		ResultSet rs = null;
		ArrayList array = new ArrayList();
		try {
			stat = connection.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				array.add(rs.getString(1));
			}
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (stat != null) try {stat.close();} catch (SQLException e) {}
		}
		return array;
	}
	
	void delRecord() throws SQLException {
		String sql = "delete from customer_password_generated";
		Statement stat = null;
		try {
			stat = connection.createStatement();
			stat.executeUpdate(sql);
		} finally {
			if (stat != null) try {stat.close();} catch (SQLException e) {}
		}
		
	}
	
	void insertPwd(ArrayList id, String[] pwd) throws SQLException {
		String sql = "insert into customer_password_generated (customer_id,password) values (?,?)";
		PreparedStatement ps = null;
		delRecord();
		try {
			ps = connection.prepareStatement(sql);
			for (int i=0; i<pwd.length; i++) {
				ps.setString(1, (String)id.get(i));
				ps.setString(2, pwd[i]);
				System.out.println((String)id.get(i) + ": " + pwd[i]);
				ps.executeUpdate();
			}
			
		} finally {
			if (ps != null) try {ps.close();} catch (SQLException e) {}
		}
	}
	
	
	void execution() {
		try {
			init();
			ArrayList id = selCustomerId();
			String[] pwd = new String[id.size()];
			System.out.println("customer total: " + pwd.length);
			for (int i=0; i<pwd.length; i++) {
				pwd[i] = genrate(8);
			}
			insertPwd(id, pwd);
			if (connection != null) connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String argv[]) {
		PasswordGenerater pg = new PasswordGenerater();
		pg.execution();
	}
}
