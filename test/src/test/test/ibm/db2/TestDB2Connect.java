package test.ibm.db2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDB2Connect {

	public static void main(String[] args) {
		TestDB2Connect.TestMain();
	}
	
	static void TestMain() {
		String user = "db2inst1";
		String password = "db2inst1";
		String url = "jdbc:db2://10.4.5.206:60000/db2afx";
		
		Connection con = null;
		Statement stat = null;
		
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			con = (Connection) DriverManager.getConnection( url, user, password );
			
			String sql = "insert into FX_MARGIN_CALLED values(" +
					"'20060406', " +
					"'001150', " +
					"4, " +
					"90004578065.00, " +
					"500000.00, " +
					"0, " +
					"0.00, " +
					"0.5, " +
					"250000.000, " +
					"2210000, " +
					"0.00, " +
					"2210000, " +
					"0.9999, " +
					"0, " +
					"0, " +
					"0, " +
					"0, " +
					"1, " +
					"timestamp('2006-04-06', '16:05:00'), " +
					"timestamp('2006-04-06', '16:05:00'))";
			
			stat = con.createStatement();
			stat.executeUpdate(sql);
			System.out.println("done!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(con != null) try {
				con.close();
			} catch (SQLException e) {}
		}
	}
}
