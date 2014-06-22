package T20;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

public class MessageTest {
	public void sendMessageJMS20(ConnectionFactory connectionFactory, Queue queue, String text) {
	   try (JMSContext context = connectionFactory.createContext();) {
	      context.createProducer().send(queue, text);
	   } catch (JMSRuntimeException ex) {
	      System.err.println("JMSRuntimeException...");
	   }
	}
	
	public String receiveMessageJMS20 (ConnectionFactory connectionFactory, Queue queue) {
	   String body=null;
	   try (JMSContext context = connectionFactory.createContext();){
	      JMSConsumer consumer = context.createConsumer(queue);
	      body = consumer.receiveBody(String.class);
	   } catch (JMSRuntimeException ex) {
	      System.err.println("receiveMessageJMS20");
	   }
	   return body;
	}
}
