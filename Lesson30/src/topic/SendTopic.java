package topic;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jms.TextMessage;
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
			Topic myTopic = pubSession.createTopic("TestTopic");
			TopicPublisher publisher = pubSession.createPublisher(myTopic);
			Topic myTopicTS = pubSession.createTopic("TopicTS");
			TopicPublisher publisherTS = pubSession.createPublisher(myTopicTS);
			connection.start();
			
			TextMessage msg = pubSession.createTextMessage();
			publisher.publish(msg);
			
			String dt = "";
			String dt2 = "";
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			dt = dateFormat.format(date);
			dt2 = dt;
			
			System.out.println("Sending ...");
			
			while (true) {
				try {
					List<DvalTI> ls = pdb.getResultTI(conn, "select * from d_valti where servdt > '" + dt + "' order by dt desc");
					
					for (int i = 0; i < ls.size(); i++) {
						if (i == 0) dt = ls.get(i).getServdt();
	
						msg.setText(ls.get(i).toString());
						publisher.publish(msg);
					}
					
					List<DvalTS> lsTS = pdb.getResultTS(conn, "select * from d_valts where servdt > '" + dt2 + "' order by dt desc");
					
					for (int i = 0; i < lsTS.size(); i++) {
						if (i == 0) dt2 = lsTS.get(i).getServdt();
	
						msg.setText(lsTS.get(i).toString());
						publisherTS.publish(msg);
					}
				} catch (Exception e) {
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
