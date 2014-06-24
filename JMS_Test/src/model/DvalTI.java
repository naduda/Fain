package model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DvalTI implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date dt;
	private int signalref;
	private double val;
	private Date servdt;
	private int rcode;
	
	public DvalTI(ResultSet rs) {
		try {
			setDt(rs.getTimestamp("dt"));
			setSignalref(rs.getInt("signalref"));
			setVal(rs.getInt("val"));
			setServdt(rs.getTimestamp("servdt"));
			setRcode(rs.getInt("rcode"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Date getDt() {
		return dt;
	}
	
	public void setDt(Date dt) {
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
	
	public Date getServdt() {
		return servdt;
	}
	
	public void setServdt(Date servdt) {
		this.servdt = servdt;
	}
	
	public int getRcode() {
		return rcode;
	}
	
	public void setRcode(int rcode) {
		this.rcode = rcode;
	}
}
