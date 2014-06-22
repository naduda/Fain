package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgresDB {
	public Connection getStatement(String host, String db, String user, String psw) {
		try {
			return DriverManager.getConnection("jdbc:postgresql://" + host +"/" + db, user, psw);
		} catch (Exception e) {
			System.err.println("getStatement ...");
		}
		return null;
	}
	
	public List<String> getResult(Connection conn, String sql) {
		String res = "";
		List<String> ls = new ArrayList<>();

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			ResultSetMetaData rsmd = rs.getMetaData();
	
			while (rs.next()) {				
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					res = "  -  " + rs.getString(i + 1) + res;
				}
				res = res.substring(5);
				ls.add(res + "|" + rs.getString(4));
				res = "";
			}

			
		} catch (Exception e) {
			System.err.println("getResult ...");
		}
		return ls;
	}
}
