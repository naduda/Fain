package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import objects.Breaker;
import objects.DigitalDevice;
import objects.DisConnectorGRND;
import objects.Disconnector;
import ui.Main;
import model.DvalTI;
import model.DvalTS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.SplitPane;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class Controller {
	
	public static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	@FXML private ToolBarController toolBarController;
	@FXML private MenuBarController menuBarController;
	@FXML private TreeController spTreeController;	
	@FXML private AlarmController alarmsController;	
	@FXML private Pane bpAlarms;
	
	public static void exitProgram() {
		System.out.println("exit");
		System.exit(0);
	}
	
	@FXML
	private void handleKeyInput(final InputEvent event) {
		System.out.println(event);
		if (event instanceof KeyEvent) {
			final KeyEvent keyEvent = (KeyEvent) event;
			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A) {
				System.out.println("ctrl+A");
			}
		}
	}
	
	private double oldAlarmsHeight;
	private boolean isHide = false;
	@FXML
	private void showAlarm(ActionEvent event) {
		SplitPane sp = ((SplitPane)bpAlarms.getParent().getParent());
		System.out.println(sp.getDividerPositions()[0] + " - " + isHide);
		if (isHide) {
			sp.setDividerPositions(oldAlarmsHeight);
		} else {
			oldAlarmsHeight = sp.getDividerPositions()[0];
			sp.setDividerPositions(1);
		}
		isHide = !isHide;
	}
	
	public void updateTI(DvalTI ti) {
		DigitalDevice tt = Main.mainScheme.getDigitalDeviceById(ti.getSignalref() + "");
		if (tt == null) return;
		
		tt.setLastDataDate(ti.getServdt());
		tt.setText(tt.getDecimalFormat().format(ti.getVal()));
		toolBarController.updateLabel(df.format(ti.getServdt()));
	}

	public  void updateTS(DvalTS ts) {
		Group tt = Main.mainScheme.getDeviceById(ts.getSignalref() + "");
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
		toolBarController.updateLabel(df.format(ts.getServdt()));
	}
	
	public static void updateSignal(int idSigal, int type_, int sec) {
		Object tt = type_ == 1 ? Main.mainScheme.getDigitalDeviceById(idSigal + "") : Main.mainScheme.getDeviceById(idSigal + "");
		if (tt == null) return;

		if (tt.getClass().getName().toLowerCase().endsWith("disconnector")) {

		} else if (tt.getClass().getName().toLowerCase().endsWith("disconnectorgrnd")) {

		} else if (tt.getClass().getName().toLowerCase().endsWith("breaker")) {
			Breaker dd = (Breaker) tt;
			dd.updateSignal(sec);
		} else if (tt.getClass().getName().toLowerCase().endsWith("digitaldevice")) {
			DigitalDevice dd = (DigitalDevice) tt;
			dd.updateSignal(sec);
		}
	}

	public TreeController getSpTreeController() {
		return spTreeController;
	}

	public ToolBarController getToolBarController() {
		return toolBarController;
	}

	public MenuBarController getMenuBarController() {
		return menuBarController;
	}
	
}
