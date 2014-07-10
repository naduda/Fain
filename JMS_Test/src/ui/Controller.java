package ui;

import java.io.File;

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
	
	public static void exitProgram() {
		System.out.println("exit");
		System.exit(0);
	}
	
	@FXML 
	protected void exitButtonAction(ActionEvent event) {
		exitProgram();
	}
	
	@FXML
	private MenuBar menuBar;
	
	final FileChooser fileChooser = new FileChooser();
	
	@FXML
	private void openScheme(ActionEvent event) {
		FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extentionFilter);

		File userDirectory = new File(ToolsPrLib.getFullPath("./schemes"));
		fileChooser.setInitialDirectory(userDirectory);
		
		File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            MainStage.lvTree.getItems().add(file.getName().split("\\.")[0]);
            Main.mainScheme = new Scheme(file.getPath());
            MainStage.bpScheme.setCenter(Main.mainScheme);
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
