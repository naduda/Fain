package model;

import java.io.Serializable;

public class DvalTS extends DvalTI implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int userref;
	private String statedt;
	private int schemeref;
	
	public DvalTS(String dt, int signalref, double val, String servdt, int rcode, 
			int userref, String statedt, int schemeref) {
		super(dt, signalref, val, servdt, rcode);
		setUserref(userref);
		setStatedt(statedt);
		setSchemeref(schemeref);
	}
	
	private String setStringWithLenght(String s, int l, String ch) {
		if (s.length() > l) {
			return s.substring(0, l);
		} else {
			String chs = "";
			for (int i = 0; i < l - s.length(); i++) {
				chs = chs + ch;
			}
			return s + chs;
		}
	}
	
	@Override
	public String toString() {
		return "TS --> [" + setStringWithLenght("" + getSignalref(), 8 , " ") + " - " + 
							setStringWithLenght(getDt(), 25, " ") + " - " + 
							setStringWithLenght("" + getVal(), 5, " ") + "]";
	}

	public int getUserref() {
		return userref;
	}
	
	public void setUserref(int userref) {
		this.userref = userref;
	}
	
	public String getStatedt() {
		return statedt;
	}
	
	public void setStatedt(String statedt) {
		this.statedt = statedt;
	}
	
	public int getSchemeref() {
		return schemeref;
	}
	
	public void setSchemeref(int schemeref) {
		this.schemeref = schemeref;
	}
}
