package topic;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.jms.Session;
import com.sun.messaging.jms.TopicConnection;

public class ReceiveTopic implements MessageListener {

	TopicSession session = null;
	TopicConnection connection = null;
	ConnectionFactory factory;
	
	ReceiveTopic() {
		try {
			factory = new com.sun.messaging.ConnectionFactory();
			factory.setProperty(ConnectionConfiguration.imqAddressList, "mq://127.0.0.1:7676,mq://127.0.0.1:7676");
			connection = (TopicConnection) factory.createTopicConnection("admin","admin");
			session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();

			Topic topic = session.createTopic("TestTopic");
			TopicSubscriber subscriber = session.createSubscriber(topic);
						
			subscriber.setMessageListener(this);
			
			Topic topicTS = session.createTopic("TopicTS");
			TopicSubscriber subscriberTS = session.createSubscriber(topicTS);
						
			subscriberTS.setMessageListener(this);
			
			while (true) {
				Thread.sleep(60000);
			}
		} catch (Exception e) {
			System.err.println("ReceiveTopic ");
		} finally {
			try {
				session.close();
		        connection.close();
		    } catch (Exception e) {
		    	System.out.println("Can't close JMS connection/session " + e.getMessage());
		    }
		}
	}
	
	public static void main(String[] args) {
		new ReceiveTopic();
	}
	
	@Override
	public void onMessage(Message msg) {
		String msgText;
		 try{
		     if (msg instanceof TextMessage){
		             msgText = ((TextMessage) msg).getText();
		            System.out.println(msgText);
		     }else{
		        System.out.println("Got a non-text message");
		     }
		 }
		 catch (JMSException e){
		      System.out.println("Error while consuming a message: " + e.getMessage());
		 }
	}

}
