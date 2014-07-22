package topic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import jdbc.PostgresDB;
import model.DvalTI;
import model.Tsignal;
import ua.pr.common.ToolsPrLib;

public class SendDValTI implements Runnable {

	private final PostgresDB pdb = new PostgresDB("193.254.232.107", "5451", "dimitrovoEU", "postgres", "askue");
	
	private boolean isRun = true;
	private ObjectMessage msgO;
	private TopicPublisher publisher;
	private List<DvalTI> ls = null;
	private Map<Integer, Tsignal> signals;
	
	public SendDValTI(TopicSession pubSession, Map<Integer, Tsignal> signals) {
		this.signals = signals;
		try {
			Topic topic = pubSession.createTopic("DvalTI");
			publisher = pubSession.createPublisher(topic);
			msgO = pubSession.createObjectMessage();
		} catch (JMSException e) {
			System.err.println("public SendDValTI(TopicSession pubSession)");
		}
	}
	
	@Override
	public void run() {
		Timestamp dt = new Timestamp(new Date().getTime());
		while (isRun) {
			try {
				ls = pdb.getLastTI(dt);
				if (ls != null) {
					for (int i = 0; i < ls.size(); i++) {
						DvalTI ti = ls.get(i);
						if (i == 0) dt = ti.getServdt();
						
						ti.setVal(ti.getVal() * signals.get(ti.getSignalref()).getKoef());
						long diff = Math.abs(ToolsPrLib.dateDiff(ti.getDt(), ti.getServdt(), 2)); //2 = minuts
						if (diff > 3) {
							ti.setActualData(false);
							System.err.println("No actual data - " + ti.getSignalref() + 
									"   [Diff = " + diff + " min;   servdt: " + 
									new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(ti.getServdt()) + "]");
						}
						msgO.setObject(ti);
						publisher.publish(msgO);
					}
				}
			} catch (Exception e) {
				System.err.println("SendDValTI");
			}
		}
	}

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
}
