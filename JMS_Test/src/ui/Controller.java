package ui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import objects.Breaker;
import objects.DigitalDevice;
import objects.DisConnectorGRND;
import objects.Disconnector;
import ua.pr.common.ToolsPrLib;
import model.DvalTI;
import model.DvalTS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.MenuBar;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
	
	public static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	@FXML private MenuBar menuBar;
	
	public static void exitProgram() {
		System.out.println("exit");
		System.exit(0);
	}
	
	@FXML 
	protected void exitButtonAction(ActionEvent event) {
		exitProgram();
	}

	final FileChooser fileChooser = new FileChooser();
	
	@FXML
	private void openScheme(ActionEvent event) {
		FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extentionFilter);

		File userDirectory = new File(ToolsPrLib.getFullPath("./schemes"));
		fileChooser.setInitialDirectory(userDirectory);
		
		File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
        	String schemeName = file.getName().split("\\.")[0];
        	int schemeIndex = MainStage.lvTree.getItems().indexOf(schemeName);
        	
	        if (schemeIndex == -1) {
	            MainStage.setScheme(schemeName);
        	}
        }
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
	
	public static void updateTI(DvalTI ti) {
		DigitalDevice tt = Main.mainScheme.getDigitalDeviceById(ti.getSignalref() + "");
		if (tt == null) return;
		
		tt.setLastDataDate(ti.getServdt());
		tt.setText(tt.getDecimalFormat().format(ti.getVal()));
		MainStage.lLastDate.setText(df.format(ti.getServdt()));
	}
	
	
	
	public static void updateTS(DvalTS ts) {
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
		MainStage.lLastDate.setText(df.format(ts.getServdt()));
	}
	
	public static void updateSignal(int idSigal, int type_, int sec) {
		Object tt = type_ == 1 ? Main.mainScheme.getDigitalDeviceById(idSigal + "") : Main.mainScheme.getDeviceById(idSigal + "");
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
