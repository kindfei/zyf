package flex.samples.census;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import flex.samples.ConnectionHelper;

public class CensusService
{
	public List getElements(int begin, int count)
	{

		long startTime = System.currentTimeMillis();

		Connection c = null;
		List list = new ArrayList();

		String sql = "SELECT id, age, classofworker, education, maritalstatus, race, sex FROM census WHERE id > ? AND id <= ? ORDER BY id ";

		try {

			c = ConnectionHelper.getConnection();
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, begin);
			stmt.setInt(2, begin + count);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				CensusEntryVO ce = new CensusEntryVO();
				ce.setId(rs.getInt("id"));
				ce.setAge(rs.getInt("age"));
				ce.setClassOfWorker(rs.getString("classofworker"));
				ce.setEducation(rs.getString("education"));
				ce.setMaritalStatus(rs.getString("maritalstatus"));
				ce.setRace(rs.getString("race"));
				ce.setSex(rs.getString("sex"));
				list.add(ce);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				c.close();
			} catch (Exception ignored) {
			}
		}

		System.out.println("Service execution time: " + (System.currentTimeMillis() - startTime));

		return list;
	}
}