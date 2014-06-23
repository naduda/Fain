package topic;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import ui.Main;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.jms.Session;
import com.sun.messaging.jms.TopicConnection;

public class ReceiveTopic implements MessageListener, Runnable {

	private TopicSession session = null;
	private TopicConnection connection = null;
	private ConnectionFactory factory;
	private boolean isRun = true;
	private String ip = "";
	
	public void setRun(boolean r) {
		isRun = r;
	}
	
	public ReceiveTopic() {
	}
	
	public ReceiveTopic(String ip) {
		this.ip = ip;
	}
	
	@Override
	public void run() {
		try {
			factory = new com.sun.messaging.ConnectionFactory();
			String prop = ip.length() == 0 ? "mq://127.0.0.1:7676,mq://127.0.0.1:7676" : "mq://" + ip + ":7676,mq://" + ip + ":7676";
			factory.setProperty(ConnectionConfiguration.imqAddressList, prop);
			connection = (TopicConnection) factory.createTopicConnection("admin","admin");
			session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();

			Topic topic = session.createTopic("TestTopic");
			TopicSubscriber subscriber = session.createSubscriber(topic);
						
			subscriber.setMessageListener(this);
			
			Topic topicTS = session.createTopic("TopicTS");
			TopicSubscriber subscriberTS = session.createSubscriber(topicTS);
						
			subscriberTS.setMessageListener(this);
			
			while (isRun) {
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
		if (args.length > 0) {
			runInNewthread(new ReceiveTopic(args[0]));
		} else {
			runInNewthread(new ReceiveTopic());
		}
	}
	
	public static void runInNewthread(Runnable runnable) { 
        Thread brokerThread = new Thread(runnable); 
        brokerThread.setDaemon(false); 
        brokerThread.start(); 
    } 
	
	@Override
	public void onMessage(Message msg) {
		String msgText;
		try {
			if (msg instanceof TextMessage) {
				msgText = ((TextMessage) msg).getText();
				if (msgText.startsWith("TI")) {
					System.out.println(msgText);
					if (msgText.indexOf("1100284") > 0) {
						Main.mainStage.getT1().setText(msgText.substring(msgText.lastIndexOf(" "), msgText.length() - 1));
						Main.mainStage.getT2().setText(msgText.substring(msgText.indexOf("  - ") + 3, msgText.lastIndexOf("  - ")));
					}
				} else {
					System.err.println(msgText);
				}
		            
		     }else{
		        System.out.println("Got a non-text message");
		     }
		 }
		 catch (JMSException e){
		      System.out.println("Error while consuming a message: " + e.getMessage());
		 }
	}

}
