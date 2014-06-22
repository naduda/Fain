package T20;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.Session;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;

public class Send2 {
	public static void main(String[] args) {
		ConnectionFactory factory;
		QueueConnection connection = null;
		Session session = null;
		
		try {
			factory = new com.sun.messaging.ConnectionFactory();  
		    factory.setProperty(ConnectionConfiguration.imqAddressList, "mq://127.0.0.1:7676,mq://127.0.0.1:7676");
		    connection = factory.createQueueConnection("admin","admin");
		    connection.start();
		    session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		    Queue ioQueue = session.createQueue("TestQueue");
		    
		    MessageTest mt = new MessageTest();
		    mt.sendMessageJMS20(factory, ioQueue, "123");
		    
		} catch (Exception e) {
			System.err.println("ConnectionFactory");
		} finally {
			try {
				session.close();
				connection.close();
			} catch (Exception e) {
				System.err.println("Can’t close JMS connection/session " + e.getMessage());
			}
	    }
	}
}
