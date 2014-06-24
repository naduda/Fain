package topic;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import model.DvalTI;
import model.DvalTS;
import jdbc.PostgresDB;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.jms.Session;
import com.sun.messaging.jms.TopicConnection;

public class SendTopic {
	public static void main(String[] args) {
		ConnectionFactory factory;
		TopicConnection connection = null;
		TopicSession pubSession = null;
		PostgresDB pdb = new PostgresDB();
//		Connection conn = pdb.getStatement("93.183.238.170:5434", "pilot", "postgres", "12345678");
		Connection conn = pdb.getConnection("193.254.232.107:5451", "dimitrovoEU", "postgres", "askue");

		try {
			factory = new com.sun.messaging.ConnectionFactory();
			factory.setProperty(ConnectionConfiguration.imqAddressList, "mq://127.0.0.1:7676,mq://127.0.0.1:7676");
			connection = (TopicConnection) factory.createTopicConnection("admin","admin");
			pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Topic tDvalTI = pubSession.createTopic("DvalTI");
			TopicPublisher publisherDvalTI = pubSession.createPublisher(tDvalTI);
			Topic tDvalTS = pubSession.createTopic("DvalTS");
			TopicPublisher publisherDvalTS = pubSession.createPublisher(tDvalTS);
			connection.start();
			
			ObjectMessage msgO = pubSession.createObjectMessage();
			
			Date dt;
			Date dt2;
			dt = new Date();
			dt2 = dt;
			
			System.out.println("Sending ...");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSS");
			while (true) {
				try {
					String sql = String.format("select * from d_valti where servdt > '%s' order by dt desc", sdf.format(dt));
					List<DvalTI> ls = pdb.getResultTI(conn, sql);
					
					for (int i = 0; i < ls.size(); i++) {
						if (i == 0) dt = ls.get(i).getServdt();				
	
						msgO.setObject(ls.get(i));
						publisherDvalTI.publish(msgO);
					}
					
					sql = String.format("select * from d_valts where servdt > '%s' order by dt desc", sdf.format(dt2));
					List<DvalTS> lsTS = pdb.getResultTS(conn, sql);
					
					for (int i = 0; i < lsTS.size(); i++) {
						if (i == 0) dt2 = lsTS.get(i).getServdt();

						msgO.setObject(lsTS.get(i));
						publisherDvalTS.publish(msgO);
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						if (conn != null) {
							conn.close();
						}
						conn = pdb.getConnection("193.254.232.107:5451", "dimitrovoEU", "postgres", "askue");
						if (conn != null) {
							System.out.println("New Connection");
						}
					} catch (Exception e1) {
						System.out.println("Connecting error " + conn);
						Thread.sleep(10000);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("SendTopic");
		} finally {
			try {
				conn.close();
				pubSession.close();
		        connection.close();
		    } catch (Exception e) {
		    	System.out.println("CanÕt close JMS connection/session " + e.getMessage());
		    }
		}
		
	}
}
