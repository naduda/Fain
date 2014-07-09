package ui;

import objects.Breaker;
import objects.DigitalDevice;
import objects.DisConnectorGRND;
import objects.Disconnector;
import model.DvalTI;
import model.DvalTS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;

public class Controller {
	
	public static void exitProgram() {
		System.out.println("exit");
		System.exit(0);
	}
	
	@FXML 
	protected void exitButtonAction(ActionEvent event) {
		exitProgram();
	}
	
	public static void updateTI(DvalTI ti) {
		DigitalDevice tt = Main.mainStage.getDigitalDeviceById(ti.getSignalref() + "");
		if (tt == null) return;
		
		tt.setLastDataDate(ti.getServdt());
		tt.setText(tt.getDecimalFormat().format(ti.getVal()));
	}
	
	public static void updateTS(DvalTS ts) {
		Group tt = Main.mainStage.getDeviceById(ts.getSignalref() + "");
		if (tt == null) return;
		
		if (tt.getClass().getName().toLowerCase().equals("objects.disconnector")) {
			Disconnector dc = (Disconnector) tt;
			dc.changeTS((int)ts.getVal());
		} else if (tt.getClass().getName().toLowerCase().equals("objects.disconnectorgrnd")) {
			DisConnectorGRND dcg = (DisConnectorGRND) tt;
			dcg.changeTS((int)ts.getVal());
		} else if (tt.getClass().getName().toLowerCase().equals("objects.breaker")) {
			Breaker br = (Breaker) tt;
			br.setLastDataDate(ts.getServdt());
			br.changeTS((int)ts.getVal());
		}
	}
	
	public static void updateSignal(int idSigal, int type_, int sec) {
		Object tt = type_ == 1 ? Main.mainStage.getDigitalDeviceById(idSigal + "") : Main.mainStage.getDeviceById(idSigal + "");
		if (tt == null) return;

		if (tt.getClass().getName().toLowerCase().equals("objects.disconnector")) {

		} else if (tt.getClass().getName().toLowerCase().equals("objects.disconnectorgrnd")) {

		} else if (tt.getClass().getName().toLowerCase().equals("objects.breaker")) {
			Breaker dd = (Breaker) tt;
			dd.updateSignal(sec);
		} else if (tt.getClass().getName().toLowerCase().equals("objects.digitaldevice")) {
			DigitalDevice dd = (DigitalDevice) tt;
			dd.updateSignal(sec);
		}
	}
}
