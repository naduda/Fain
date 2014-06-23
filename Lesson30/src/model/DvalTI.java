package model;

public class DvalTI {
	private String dt;
	private int signalref;
	private double val;
	private String servdt;
	private int rcode;
	
	public DvalTI(String dt, int signalref, double val, String servdt, int rcode) {
		setDt(dt);
		setSignalref(signalref);
		setVal(val);
		setServdt(servdt);
		setRcode(rcode);
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
		return "TI --> [" + setStringWithLenght("" + getSignalref(), 8 , " ") + " - " + 
							setStringWithLenght(getDt(), 25, " ") + " - " + 
							setStringWithLenght("" + getVal(), 5, " ") + "]";
	}


	public String getDt() {
		return dt;
	}
	
	public void setDt(String dt) {
		this.dt = dt;
	}
	
	public int getSignalref() {
		return signalref;
	}
	
	public void setSignalref(int signalref) {
		this.signalref = signalref;
	}
	
	public double getVal() {
		return val;
	}
	
	public void setVal(double val) {
		this.val = val;
	}
	
	public String getServdt() {
		return servdt;
	}
	
	public void setServdt(String servdt) {
		this.servdt = servdt;
	}
	
	public int getRcode() {
		return rcode;
	}
	
	public void setRcode(int rcode) {
		this.rcode = rcode;
	}
}
