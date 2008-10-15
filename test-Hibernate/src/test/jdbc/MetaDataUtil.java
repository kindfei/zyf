package test.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class MetaDataUtil {

	public static void printInfos(Connection conn) throws SQLException {
		System.out.println("Connection=" + conn.getClass().getName());
		System.out.println("autoCommit=" + conn.getAutoCommit());
		System.out.println("catalog=" + conn.getCatalog());
//		System.out.println("clientInfo=" + conn.getClientInfo());
		System.out.println("holdability=" + conn.getHoldability());
		System.out.println("readOnly=" + conn.isReadOnly());
		
		DatabaseMetaData data = conn.getMetaData();
		System.out.println("MetaData.URL=" + data.getURL());
		System.out.println("MetaData.catalogSeparator=" + data.getCatalogSeparator());
		System.out.println("MetaData.catalogTerm=" + data.getCatalogTerm());
		System.out.println("MetaData.databaseMajorVersion=" + data.getDatabaseMajorVersion());
		System.out.println("MetaData.databaseMinorVersion=" + data.getDatabaseMinorVersion());
		System.out.println("MetaData.databaseProductName=" + data.getDatabaseProductName());
		System.out.println("MetaData.databaseProductVersion=" + data.getDatabaseProductVersion());
		System.out.println("MetaData.defaultTransactionIsolation=" + data.getDefaultTransactionIsolation());
		System.out.println("MetaData.driverMajorVersion=" + data.getDriverMajorVersion());
		System.out.println("MetaData.driverMinorVersion=" + data.getDriverMinorVersion());
		System.out.println("MetaData.driverName=" + data.getDriverName());
		System.out.println("MetaData.driverVersion=" + data.getDriverVersion());
		System.out.println("MetaData.extraNameCharacters=" + data.getExtraNameCharacters());
		System.out.println("MetaData.identifierQuoteString=" + data.getIdentifierQuoteString());
		System.out.println("MetaData.JDBCMajorVersion=" + data.getJDBCMajorVersion());
		System.out.println("MetaData.JDBCMinorVersion=" + data.getJDBCMinorVersion());
	}
	
}
