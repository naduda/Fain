package ui.alarm;

import java.text.SimpleDateFormat;

import model.Alarm;
import javafx.beans.property.SimpleStringProperty;

public class AlarmTableItem {

	private final SimpleDateFormat dFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
	
	private final SimpleStringProperty pObject;
	private final SimpleStringProperty pLocation;
	private final SimpleStringProperty pAlarmName;
	private final SimpleStringProperty pRecordDT;
	private final SimpleStringProperty pEventDT;
	private final SimpleStringProperty pAlarmMes;
	private final SimpleStringProperty pLogState;
	private final SimpleStringProperty pConfirmDT;
	private final SimpleStringProperty pUserRef;
	private final SimpleStringProperty pLogNote;
	private final SimpleStringProperty pAlarmPriority;
	private final SimpleStringProperty pEventType;
	private final SimpleStringProperty pSchemeObject;
	
	public AlarmTableItem(Alarm a) {
		pObject = new SimpleStringProperty(a.getpObject());
		pLocation = new SimpleStringProperty(a.getpLocation());
		pAlarmName = new SimpleStringProperty(a.getAlarmname());
		pRecordDT = new SimpleStringProperty(dFormat.format(a.getRecorddt()));
		pEventDT = new SimpleStringProperty(dFormat.format(a.getEventdt()));
		pAlarmMes = new SimpleStringProperty(a.getAlarmmes());
		pLogState = new SimpleStringProperty(a.getpLogState());
		pConfirmDT = new SimpleStringProperty("");
		pUserRef = new SimpleStringProperty(a.getUserref() + "");
		pLogNote = new SimpleStringProperty(a.getLognote());
		pAlarmPriority = new SimpleStringProperty(a.getpAlarmPriority());
		pEventType = new SimpleStringProperty(a.getpEventType());
		pSchemeObject = new SimpleStringProperty("");
	}
	
	public String getPObject() {
        return pObject.get();
    }

    public void setPObject(String sObject) {
    	pObject.set(sObject);
    }
    
    public String getPLocation() {
        return pLocation.get();
    }

    public void setPLocation(String sLocation) {
    	pLocation.set(sLocation);
    }
    
    public String getPAlarmName() {
        return pAlarmName.get();
    }

    public void setPAlarmName(String sAlarmName) {
    	pAlarmName.set(sAlarmName);
    }
    
    public String getPRecordDT() {
        return pRecordDT.get();
    }

    public void setPRecordDT(String sRecordDT) {
    	pRecordDT.set(sRecordDT);
    }
    
    public String getPEventDT() {
        return pEventDT.get();
    }

    public void setPEventDT(String sEventDT) {
    	pEventDT.set(sEventDT);
    }
    
    public String getPAlarmMes() {
        return pAlarmMes.get();
    }

    public void setPAlarmMes(String sAlarmMes) {
    	pAlarmMes.set(sAlarmMes);
    }
    
    public String getPLogState() {
        return pLogState.get();
    }

    public void setPLogState(String sLogState) {
    	pLogState.set(sLogState);
    }
    
    public String getPConfirmDT() {
        return pConfirmDT.get();
    }

    public void setPConfirmDT(String sConfirmDT) {
    	pConfirmDT.set(sConfirmDT);
    }
    
    public String getPUserRef() {
        return pUserRef.get();
    }

    public void setPUserRef(String sUserRef) {
    	pUserRef.set(sUserRef);
    }
    
    public String getPLogNote() {
        return pLogNote.get();
    }

    public void setPLogNote(String sLogNote) {
    	pLogNote.set(sLogNote);
    }
    
    public String getPAlarmPriority() {
        return pAlarmPriority.get();
    }

    public void setPAlarmPriority(String sAlarmPriority) {
    	pAlarmPriority.set(sAlarmPriority);
    }
    
    public String getPEventType() {
        return pEventType.get();
    }

    public void setPEventType(String sEventType) {
    	pEventType.set(sEventType);
    }
    
    public String getPSchemeObject() {
        return pSchemeObject.get();
    }

    public void setPSchemeObject(String sSchemeObject) {
    	pSchemeObject.set(sSchemeObject);
    }


}
