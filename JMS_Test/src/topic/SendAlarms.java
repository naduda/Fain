package topic;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import jdbc.PostgresDB;
import model.Alarm;
import model.TSysParam;
import model.Tsignal;

public class SendAlarms implements Runnable {

	private final PostgresDB pdb = new PostgresDB("193.254.232.107", "5451", "dimitrovoEU", "postgres", "askue");
	private final Map<String, TSysParam> sysParamsEvent = pdb.getTSysParamMap("ALARM_EVENT");
	private final Map<String, TSysParam> sysParamsPriority = pdb.getTSysParamMap("ALARM_PRIORITY");
	private final Map<String, TSysParam> sysParamsLogState = pdb.getTSysParamMap("LOG_STATE");
	
	private boolean isRun = true;
	private ObjectMessage msgO;
	private TopicPublisher publisher;
	private List<Alarm> ls = null;
	private Map<Integer, Tsignal> signals;
	
	public SendAlarms(TopicSession pubSession, Map<Integer, Tsignal> signals) {
		this.signals = signals;
		try {
			Topic topic = pubSession.createTopic("Alarms");
			publisher = pubSession.createPublisher(topic);
			msgO = pubSession.createObjectMessage();
		} catch (JMSException e) {
			System.err.println("public SendAlarms(TopicSession pubSession)");
		}
	}
	
	@Override
	public void run() {
		Timestamp dt = new Timestamp(new Date().getTime());
		while (isRun) {
			try {
				ls = pdb.getAlarms(dt);
				if (ls != null) {
					for (int i = 0; i < ls.size(); i++) {
						Alarm a = ls.get(i);
						if (i == 0) dt = a.getEventdt();

						a.setpObject(signals.get(a.getObjref()).getNamesignal());
						a.setpLocation(signals.get(a.getObjref()).getLocation());
						
				    	String sAlarmMes = a.getAlarmmes();
				    	if (sAlarmMes.toLowerCase().startsWith("<vf::")) {
				    		sAlarmMes = sAlarmMes.substring(sAlarmMes.indexOf(":" + (int)a.getObjval()) + 1);
				    		sAlarmMes = sAlarmMes.substring(sAlarmMes.indexOf("=") + 1, sAlarmMes.indexOf(":"));
				    		a.setAlarmmes(sAlarmMes);
				    	}
				    	
				    	a.setpEventType(sysParamsEvent.get("" + a.getEventtype()).getParamdescr());
				    	a.setpAlarmPriority(sysParamsPriority.get("" + a.getAlarmpriority()).getParamdescr());
				    	a.setpLogState(sysParamsLogState.get("" + a.getLogstate()).getParamdescr());
				    	
						msgO.setObject(a);
						publisher.publish(msgO);
					}
				}
			} catch (Exception e) {
				System.err.println("SendAlarms");
				e.printStackTrace();
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
