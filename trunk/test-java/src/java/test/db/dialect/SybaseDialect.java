package test.db.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.type.StringType;

/**
 * 
 * @author yz69579
 *
 */
public class SybaseDialect extends org.hibernate.dialect.SybaseDialect {
	public SybaseDialect() {
		super();
		
		registerColumnType(Types.CHAR, "char($l)" );
		
		registerHibernateType(Types.CHAR, TrimmedStringType.class.getName());
		registerHibernateType(Types.LONGVARCHAR, Hibernate.STRING.getName());  
		registerHibernateType(Types.LONGVARBINARY, Hibernate.BINARY.getName()); 
	}
	
	public static class TrimmedStringType extends StringType {
		private static final long serialVersionUID = 1L;

		@Override
		public Object get(ResultSet rs, String name) throws SQLException {
			String r = (String) super.get(rs, name);
			return r == null ? r : r.trim();
		}
	}
}
