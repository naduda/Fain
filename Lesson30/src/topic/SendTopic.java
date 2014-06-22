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
		Connection conn = pdb.getStatement("193.254.232.107:5451", "dimitrovoEU", "postgres", "askue");

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
				List<String> ls = pdb.getResult(conn, "select * from d_valti where dt > '" + dt + "' order by dt desc");
				
				for (int i = 0; i < ls.size(); i++) {
					String msgs = ls.get(i);
					if (i == 0) dt = msgs.substring(msgs.indexOf("|") + 1);

					msg.setText("TI -->" + msgs.substring(0, msgs.indexOf("|")));
					publisher.publish(msg);
				}
				
				ls = pdb.getResult(conn, "select * from d_valts where dt > '" + dt2 + "' order by dt desc");
				
				for (int i = 0; i < ls.size(); i++) {
					String msgs = ls.get(i);
					if (i == 0) dt2 = msgs.substring(msgs.indexOf("|") + 1);

					msg.setText("TS -->" + msgs.substring(0, msgs.indexOf("|")));
					publisherTS.publish(msg);
				}
				
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.err.println("SendTopic");
		} finally {
			try {
				conn.close();
				pubSession.close();
		        connection.close();
		    } catch (Exception e) {
		    	System.out.println("Can’t close JMS connection/session " + e.getMessage());
		    }
		}
		
	}
}
