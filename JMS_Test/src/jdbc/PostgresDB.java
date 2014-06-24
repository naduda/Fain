package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.DvalTI;
import model.DvalTS;
import model.Tsignal;

public class PostgresDB {
	public Connection getConnection(String host, String db, String user, String psw) {
		try {
			return DriverManager.getConnection("jdbc:postgresql://" + host +"/" + db, user, psw);
		} catch (Exception e) {
			System.err.println("Connecting error");
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
			ls = null;
			System.err.println("getResult ...");
		}
		return ls;
	}
	
	public List<DvalTI> getResultTI(Connection conn, String sql) {
		List<DvalTI> ls = new ArrayList<>();

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				ls.add(new DvalTI(rs.getString("dt"), rs.getInt("signalref"), rs.getDouble("val"), rs.getString("servdt"), rs.getInt("rcode")));
			}
	
		} catch (Exception e) {
			ls = null;
			System.err.println("getResultTI ...");
		}
		return ls;
	}
	
	public List<DvalTS> getResultTS(Connection conn, String sql) {
		List<DvalTS> ls = new ArrayList<>();

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				ls.add(new DvalTS(rs.getString("dt"), rs.getInt("signalref"), rs.getDouble("val"), rs.getString("servdt"), rs.getInt("rcode"),
						rs.getInt("userref"), rs.getString("statedt"), rs.getInt("schemeref")));
			}
	
		} catch (Exception e) {
			ls = null;
			System.err.println("getResultTS ...");
		}
		return ls;
	}
	
	public List<Tsignal> getAllSignals(Connection conn) {
		List<Tsignal> ls = new ArrayList<>();

		String sql = "select * from t_signal where status = 1 and typesignalref in (1, 2) and namesignal not like '%Ñגÿחü ס ףסענמיסעגמל%' order by namesignal";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql);) {
			while (rs.next()) {
				ls.add(new Tsignal(rs));
			}
		} catch (Exception e) {
			ls = null;
			System.err.println("getAllSignals ...");
		}
		return ls;
	}
}
