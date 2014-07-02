package topic;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import ua.pr.common.ToolsPrLib;
import model.DvalTI;
import model.DvalTS;
import model.Tsignal;
import jdbc.PostgresDB;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.jms.Session;
import com.sun.messaging.jms.TopicConnection;

public class SendTopic {
//	private static final PostgresDB pdb = new PostgresDB("10.1.3.17", "3700", "dimitrovEU");
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
			
			Topic tDvalTI = pubSession.createTopic("DvalTI");
			TopicPublisher publisherDvalTI = pubSession.createPublisher(tDvalTI);
			Topic tDvalTS = pubSession.createTopic("DvalTS");
			TopicPublisher publisherDvalTS = pubSession.createPublisher(tDvalTS);
			connection.start();
			
			ObjectMessage msgO = pubSession.createObjectMessage();
			
			Timestamp dt;
			Timestamp dt2;
			dt = new Timestamp(new Date().getTime());
			dt2 = dt;

			Map<Integer, Tsignal> signals = pdb.getTsignalsMap();	
			if (signals == null) {
				System.out.println("Can't get signals ...");
				System.exit(0);
			}
			
			System.out.println("Sending ...");		
			List<DvalTI> ls = null;
			List<DvalTS> lsTS = null;
			while (true) {
				try {
					ls = pdb.getLastTI(dt);
					if (ls != null) {
						for (int i = 0; i < ls.size(); i++) {
							DvalTI ti = ls.get(i);
							if (i == 0) dt = ti.getServdt();
							
							ti.setVal(ti.getVal() * signals.get(ti.getSignalref()).getKoef());
							long diff = Math.abs(ToolsPrLib.dateDiff(ti.getDt(), ti.getServdt(), 1));
							if (diff > 60) {
								ti.setActualData(false);
								System.err.println("No actual data - " + ti.getSignalref() + "      Diff > 60 s;     --> " + diff);
							}
							msgO.setObject(ti);
							publisherDvalTI.publish(msgO);
						}
					} else {
						System.out.println("null");
						Thread.sleep(10000);
					}
					
					lsTS = pdb.getLastTS(dt2);
					if (lsTS != null) {
						for (int i = 0; i < lsTS.size(); i++) {
							DvalTS ts = lsTS.get(i);
							if (i == 0) dt2 = ts.getServdt();
	
							msgO.setObject(ts);
							publisherDvalTS.publish(msgO);
						}
					}
				} catch (Exception e) {
					System.out.println("SendTopic/While");
				}
			}
		} catch (Exception e) {
			System.err.println("SendTopic");
		} finally {
			try {
				pubSession.close();
		        connection.close();
		    } catch (Exception e) {
		    	System.out.println("CanÕt close JMS connection/session " + e.getMessage());
		    }
		}
		
	}
}
