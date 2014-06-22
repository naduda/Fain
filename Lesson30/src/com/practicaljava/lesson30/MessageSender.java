package com.practicaljava.lesson30;

import javax.jms.*;

import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.ConnectionConfiguration;


public class MessageSender {
 public static void main(String[] args){
	 Session session=null;
	 ConnectionFactory factory;
	 QueueConnection connection=null;
	 
	 try{
	      factory = new com.sun.messaging.ConnectionFactory();  
	      factory.setProperty(ConnectionConfiguration.imqAddressList, "mq://127.0.0.1:7676,mq://127.0.0.1:7676");
	      connection = factory.createQueueConnection("admin","admin");
	      
	      connection.start();

	      session = connection.createQueueSession( 
	                          false, Session.AUTO_ACKNOWLEDGE);
	      Queue ioQueue = session.createQueue("TestQueue");
	      MessageProducer queueSender = session.createProducer(ioQueue);
	      
	      // Buy 200 shares of IBM at market price	      
	      TextMessage outMsg = session.createTextMessage("IBM 200 Mkt");
	      queueSender.send(outMsg);
	      
	      for (int i = 0; i < 50; i++) {
	    	  outMsg = session.createTextMessage("   - " + i);
	    	  queueSender.send(outMsg);
	    	  try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	      }
	     
	      
	      queueSender.close();
	      
	      System.out.println("Sucsessfully placed an order to purchase 200 shares of IBM");
	    }
	    catch (JMSException e){
	           System.out.println("Error: " + e.getMessage());
	    } 
	    finally{
	       try{
	          session.close();
	          connection.close();
	       } catch (Exception e) {
	          System.out.println("CanÕt close JMS connection/session " + e.getMessage());
	      }
	    }	
}	
	
}
