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
import ui.Main;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.jms.Session;
import com.sun.messaging.jms.TopicConnection;

public class ReceiveTopic implements MessageListener {

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
		run();		
	}
	
	public ReceiveTopic(String ip) {
		this.ip = ip;
		run();
	}
	
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
				System.out.println(dateFormat.format(new Date()) + "   --------------------------");
			}
		} catch (Exception e) {
			System.err.println("ReceiveTopic ");
			e.printStackTrace();
		} finally {
			try {
				session.close();
		        connection.close();
		    } catch (Exception e) {
		    	System.out.println("Can't close JMS connection/session " + e.getMessage());
		    }
		}
	}	
	
	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof ObjectMessage) {				
				Object obj = ((ObjectMessage)msg).getObject();
		    	if (obj.getClass().getName().toLowerCase().equals("model.dvalti")) {
		    		DvalTI ti = (DvalTI) obj;
		    		if (Main.mainStage != null) {
		    			Text tt = Main.mainStage.getTextById(((DvalTI)obj).getSignalref() + "");

		    			tt.setText(decimalFormat.format(ti.getVal()) + "     " + dateFormat.format(ti.getDt()) + "     " + dateFormat.format(ti.getServdt()));

		    			if ((System.currentTimeMillis() - ti.getServdt().getTime()) < 11000) {			    				
		    				tt.setFill(Color.GREEN);
		    			} else {
		    				tt.setFill(Color.RED);
		    			}
		    		}
		    	} else if (obj.getClass().getName().toLowerCase().equals("model.dvalts")) {
		    		if (Main.mainStage != null) {
		    			DvalTS ts = (DvalTS) obj;
		    			Button tt = Main.mainStage.getButtontById(ts.getSignalref() + "");
		    			Text ttd = Main.mainStage.getTextById("d_" + ts.getSignalref());
		    			if (ttd != null) {
		    				ttd.setText(dateFormat.format(ts.getDt()) + "     " + dateFormat.format(ts.getServdt()));
		    				if ((System.currentTimeMillis() - ts.getServdt().getTime()) < 610000) {			    				
			    				ttd.setFill(Color.GREEN);
			    			} else {
			    				ttd.setFill(Color.RED);
			    			}
		    			}

			    		if (tt != null) {
			    			if (ts.getVal() == 0) {
			    				tt.setStyle("-fx-background-color: green;");
			    			} else if (ts.getVal() == 1) {
			    				tt.setStyle("-fx-background-color: red;");
			    			}
			    		}
			    	}
		    	}
		        
		     }
		 } catch (Exception e){
		      System.out.println("Error while consuming a message: " + e.getMessage());
		      e.printStackTrace();
		 }
	}
	
}
