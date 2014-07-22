package topic;

import java.util.Map;

import javax.jms.TopicSession;

import jdbc.PostgresDB;
import model.ConfTree;
import model.Tsignal;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.jms.Session;
import com.sun.messaging.jms.TopicConnection;

public class SendTopic {

	private static final PostgresDB pdb = new PostgresDB("193.254.232.107", "5451", "dimitrovoEU", "postgres", "askue");
	
	public static void main(String[] args) {
//		org.apache.log4j.BasicConfigurator.configure();
		ConnectionFactory factory;
		TopicConnection connection = null;
		TopicSession pubSession = null;
		
		try {
			factory = new com.sun.messaging.ConnectionFactory();
			factory.setProperty(ConnectionConfiguration.imqAddressList, "mq://127.0.0.1:7676,mq://127.0.0.1:7676");
			connection = (TopicConnection) factory.createTopicConnection("admin","admin");
			pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			
			connection.start();

			long s = System.currentTimeMillis();
			Map<Integer, Tsignal> signals = pdb.getTsignalsMap();
			Map<Integer, ConfTree> confTree = pdb.getConfTreeMap();
			
			for (Tsignal sign : signals.values()) {
				ConfTree ct = confTree.get(sign.getNoderef());
				String location = ct.getNodename();
				while (ct.getParentref() > 0) {
					ct = confTree.get(ct.getParentref());
					location = location + "/" + ct.getNodename();
				}
				sign.setLocation(location);
			}
			System.out.println(System.currentTimeMillis() - s);
			
			new Thread(new SendDValTI(pubSession, signals), "SendDValTI_Thread").start();
			new Thread(new SendDValTS(pubSession), "SendDValTS_Thread").start();
			new Thread(new SendAlarms(pubSession, signals), "SendAlarms_Thread").start();

			System.out.println("Sending ...");
			while (true) {

			}
		} catch (Exception e) {
			System.err.println("SendTopic");
			e.printStackTrace();
		} finally {
			try {
				pubSession.close();
		        connection.close();
		    } catch (Exception e) {
		    	System.out.println("Can't close JMS connection/session " + e.getMessage());
		    }
		}
		
	}
}
