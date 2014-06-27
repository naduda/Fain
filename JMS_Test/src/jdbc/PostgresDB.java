package jdbc;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import model.DvalTI;
import model.DvalTS;
import model.Tsignal;

public class PostgresDB {

	private DataSource dataSource;
	private SqlSession session;
	private SqlSessionFactory sqlSessionFactory;
	
	public PostgresDB (String hostJNDI, String portJNDI, String dataSourceName) {
		try {
			Properties env = new Properties();
			env.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
			env.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
			env.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			env.setProperty("org.omg.CORBA.ORBInitialHost", hostJNDI);
			env.setProperty("org.omg.CORBA.ORBInitialPort", portJNDI);
			
			InitialContext ctx = new InitialContext(env);

			dataSource = (DataSource) ctx.lookup(dataSourceName);
			
			TransactionFactory transactionFactory = new JdbcTransactionFactory();
			Environment environment = new Environment("development", transactionFactory, dataSource);
			Configuration configuration = new Configuration(environment);
			configuration.addMapper(IMapper.class);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public List<Tsignal> getSignals() {
		try {
			session = sqlSessionFactory.openSession();
			return session.getMapper(IMapper.class).getTsignals();
		} catch (Exception e) {
			System.out.println("getSignals");
			return null;
		} finally {
			session.close();
		}
	}
	
	public Map<Integer, Tsignal> getTsignalsMap() {
		try {
			session = sqlSessionFactory.openSession();
			return session.getMapper(IMapper.class).getTsignalsMap();
		} catch (Exception e) {
			System.out.println("getTsignalsMap");			
			return null;
		} finally {
			session.close();
		}
	}
	
	public List<DvalTI> getLastTI(Timestamp servdt) {
		try {
			session = sqlSessionFactory.openSession();
			return session.getMapper(IMapper.class).getLastTI(servdt);
		} catch (Exception e) {
			System.out.println("getLastTI");
			return null;
		} finally {
			session.close();
		}
	}
	
	public List<DvalTS> getLastTS(Timestamp servdt) {
		try {
			session = sqlSessionFactory.openSession();
			return session.getMapper(IMapper.class).getLastTS(servdt);
		} catch (Exception e) {
			System.out.println("getLastTI");
			return null;
		} finally {
			session.close();
		}
	}

}
