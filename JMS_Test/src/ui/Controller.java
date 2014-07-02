package ui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.DvalTI;
import model.DvalTS;
import model.Tsignal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Controller {
	private static final DecimalFormat decimalFormat = new DecimalFormat("000.000", new DecimalFormatSymbols());
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static void exitProgram() {
		System.out.println("exit");
		System.exit(0);
	}
	
	@FXML 
	protected void exitButtonAction(ActionEvent event) {
		exitProgram();
	}

	public static void updateTI(DvalTI ti) {
		Text tt = Main.mainStage.getTextById(ti.getSignalref() + "");
		
		tt.setText(decimalFormat.format(ti.getVal()) + "     " + dateFormat.format(ti.getDt()) + "     " + dateFormat.format(ti.getServdt()));
		
		if ((System.currentTimeMillis() - ti.getServdt().getTime()) < 11000) {			    				
			tt.setFill(Color.GREEN);
		} else {
			tt.setFill(Color.RED);
		}
	}
	
	public static void updateTS(DvalTS ts) {
		Button tt = Main.mainStage.getButtontById(ts.getSignalref() + "");
		Text ttd = Main.mainStage.getTextById("d_" + ts.getSignalref());
		if (ttd != null) {
			ttd.setText(dateFormat.format(ts.getDt()) + "     " + dateFormat.format(ts.getServdt()));
			if ((System.currentTimeMillis() - ts.getServdt().getTime()) < 610000) {			    				
				ttd.setFill(Color.GREEN);
			} else {
				ttd.setFill(Color.RED);
			}
		}

		if (tt != null) {
			if (ts.getVal() == 0) {
				tt.setStyle("-fx-background-color: green;");
			} else if (ts.getVal() == 1) {
				tt.setStyle("-fx-background-color: red;");
			}
		}
	}
	
	public static void updateSignal(Tsignal ts, int typeSignal, int sec) {
		Text t = typeSignal == 1 ? Main.mainStage.getTextById(ts.getIdsignal() + "") : Main.mainStage.getTextById("d_" + ts.getIdsignal());
		
		String st = t.getText();
		if (st.lastIndexOf("2014-") != -1) {
			st = st.substring(st.lastIndexOf("   "), st.length()).trim();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(st);
			} catch (ParseException e) {
				System.out.println("Error : date = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss').parse(st);");
			}
			
			if ((System.currentTimeMillis() - date.getTime()) < sec * 1000) {
				t.setFill(Color.GREEN);	
			} else {
				t.setFill(Color.RED);
			}
		}
	}
}
