package topic;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import jdbc.PostgresDB;
import model.DvalTS;

public class SendDValTS implements Runnable {

	private final PostgresDB pdb = new PostgresDB("193.254.232.107", "5451", "dimitrovoEU", "postgres", "askue");
	
	private boolean isRun = true;
	private ObjectMessage msgO;
	private TopicPublisher publisher;
	private List<DvalTS> ls = null;
	
	public SendDValTS(TopicSession pubSession) {
		try {
			Topic topic = pubSession.createTopic("DvalTS");
			publisher = pubSession.createPublisher(topic);
			msgO = pubSession.createObjectMessage();
		} catch (JMSException e) {
			System.err.println("public SendDValTS(TopicSession pubSession)");
		}
	}
	
	@Override
	public void run() {
		Timestamp dt = new Timestamp(new Date().getTime());
		while (isRun) {
			try {
				ls = pdb.getLastTS(dt);
				if (ls != null) {
					for (int i = 0; i < ls.size(); i++) {
						DvalTS ts = ls.get(i);
						if (i == 0) dt = ts.getServdt();

						msgO.setObject(ts);
						publisher.publish(msgO);
					}
				}
			} catch (Exception e) {
				System.err.println("SendDValTS");
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
