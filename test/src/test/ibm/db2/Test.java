package test.ibm.db2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	private static String fx1 = "jdbc:db2://10.4.5.28:50000/db2db";
	private static String fx2 = "jdbc:db2://10.4.5.204:60000/db2op";
	
	
	public static void main(String[] args) {
		Connection conn = null;
		Statement stat = null;
		DatabaseMetaData meta = null;
		ResultSet result = null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(fx2, "db2inst1", "db2inst1");
			stat = conn.createStatement();
			meta = conn.getMetaData();
			
			result = meta.getSchemas();
			showDetail(result);
			
			meta.getCatalogs();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {stat.close();} catch (Exception e) {}
			try {stat.close();} catch (Exception e) {}
			try {conn.close();} catch (Exception e) {}
		}
	}
	
	public static void showDetail(ResultSet result) {
		try {
			while (result.next()) {
				System.out.println(result.getString("TABLE_SCHEM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
