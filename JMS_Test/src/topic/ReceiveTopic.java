package topic;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import model.DvalTI;
import model.DvalTS;
import model.Tsignal;
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
	private DecimalFormat decimalFormat;
	private SimpleDateFormat dateFormat;
	
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
			
			Topic tDvalTI = session.createTopic("DvalTI");
			TopicSubscriber subscriberDvalTI = session.createSubscriber(tDvalTI);						
			subscriberDvalTI.setMessageListener(this);
			
			Topic tDvalTS = session.createTopic("DvalTS");
			TopicSubscriber subscriberDvalTS = session.createSubscriber(tDvalTS);						
			subscriberDvalTS.setMessageListener(this);
			
			DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
			decimalFormatSymbols.setDecimalSeparator('.');
			decimalFormatSymbols.setGroupingSeparator(' ');
			decimalFormat = new DecimalFormat("000.000", decimalFormatSymbols);
			
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
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
			Main.runInNewthread(new ReceiveTopic(args[0]));
		} else {
			Main.runInNewthread(new ReceiveTopic());
		}
	}	
	 
	
	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof ObjectMessage) {
		    	 Object obj = ((ObjectMessage)msg).getObject();
		    	 if (obj.getClass().getName().toLowerCase().equals("model.dvalti")) {
		    		if (Main.mainStage != null) {
		    			Text tt = Main.mainStage.getTextById(((DvalTI)obj).getSignalref() + "");
		    			double koef = ((Tsignal)tt.getUserData()).getKoef();
		    			koef = koef * ((DvalTI)obj).getVal();

		    			tt.setText(decimalFormat.format(koef) + "     " + dateFormat.format(((DvalTI)obj).getDt()) + "     " + dateFormat.format(((DvalTI)obj).getServdt()));
		    			String st = tt.getText();
		    			if (st.lastIndexOf("2014") != -1) {
							st = st.substring(st.lastIndexOf("2014-"), st.length());
							Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(st);
			    			if ((System.currentTimeMillis() - date.getTime()) < 11000) {			    				
			    				tt.setFill(Color.GREEN);
			    			} else {
			    				tt.setFill(Color.RED);
			    			}
		    			}
		    		}
		    	 } else if (obj.getClass().getName().toLowerCase().equals("model.dvalts")) {
		    		 if (Main.mainStage != null) {
			    			Button tt = Main.mainStage.getButtontById(((DvalTS)obj).getSignalref() + "");
			    			Text ttd = Main.mainStage.getTextById("d_" + ((DvalTI)obj).getSignalref());
			    			ttd.setText(dateFormat.format(((DvalTI)obj).getDt()) + "     " + dateFormat.format(((DvalTI)obj).getServdt()));
			    			String st = ttd.getText();
			    			if (st.lastIndexOf("2014") != -1) {
								st = st.substring(st.lastIndexOf("2014-"), st.length());
								Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(st);
				    			if ((System.currentTimeMillis() - date.getTime()) < 11000) {			    				
				    				ttd.setFill(Color.GREEN);
				    			} else {
				    				ttd.setFill(Color.RED);
				    			}
			    			}
				    		if (tt != null) {
				    			if (((DvalTI)obj).getVal() == 0) {
				    				tt.setStyle("-fx-background-color: green;");
				    			} else if (((DvalTI)obj).getVal() == 1) {
				    				tt.setStyle("-fx-background-color: red;");
				    			}
				    		}
			    		}
		    	 }
		        
		     }
		 }
		 catch (Exception e){
		      System.out.println("Error while consuming a message: " + e.getMessage());
		 }
	}
	
}
