package T20;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;

public class Receive2 implements MessageListener {

	Session session=null;
	ConnectionFactory factory;
	QueueConnection connection=null;
	MessageConsumer consumer=null;
	
	Receive2(){
		 try {
			 factory = new com.sun.messaging.ConnectionFactory();  
		     factory.setProperty(ConnectionConfiguration.imqAddressList, "mq://localhost:7676,mq://localhost:7677");
		     connection = factory.createQueueConnection("admin","admin");
		      
		     connection.start();

		     Session session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		     Queue ioQueue = session.createQueue( "TestQueue" );

		     consumer = session.createConsumer(ioQueue);
		     consumer.setMessageListener(this);
		      
		     System.out.println("Listening to the TestQueue...");
		      
		      // Don't finish -  wait for messages
		     Thread.sleep(100000);
		    
		   } catch (InterruptedException|JMSException e){
	           System.err.println("Error: " + e.getMessage());
	       } finally {
		       try {
		          session.close();
		          connection.close();
		       } catch (Exception e) {
		          System.err.println("Can’t close JMS connection/session " + e.getMessage());
		       }
		   }	
	
	}
	
	public static void main(String[] args) {
		new Receive2();
	}
	 
	@Override
	public void onMessage(Message msg) {
		String msgText;
		try {
			if (msg instanceof TextMessage) {
				msgText = ((TextMessage) msg).getText();
				System.out.println("Got from the queue: " + msgText);
			} else {
		    	System.out.println("Got a non-text message");
		    }
		} catch (JMSException e) {
			System.out.println("Error while consuming a message: " + e.getMessage());
		} 
	}

}
